#include <stdio.h>
#include <stdlib.h>
#include <fcntl.h>
#include <unistd.h>
#include <uv.h>

#if 0
void on_read(uv_fs_t *req);

uv_fs_t open_req;
uv_fs_t read_req;
uv_fs_t write_req;

char buffer[1024];

void on_write(uv_fs_t *req) {
    uv_fs_req_cleanup(req);
    if (req->result < 0) {
        fprintf(stderr, "Write error: %s\n", uv_strerror(req->result));
    }
    else {
        uv_fs_read(uv_default_loop(), &read_req, open_req.result, buffer, sizeof(buffer), -1, on_read);
    }
}

void on_read(uv_fs_t *req) {
    uv_fs_req_cleanup(req);
    if (req->result < 0) {
        fprintf(stderr, "Read error: %s\n", uv_strerror(req->result));
    }
    else if (req->result == 0) {
        uv_fs_t close_req;
        /* synchronous */
        uv_fs_close(uv_default_loop(), &close_req, open_req.result, NULL);
    }
    else {
        uv_fs_write(uv_default_loop(), &write_req, 1, buffer, req->result, -1, on_write);
    }
}

void on_open(uv_fs_t *req) {
    if (req->result != -1) {
        uv_fs_read(uv_default_loop(), &read_req, req->result,
                   buffer, sizeof(buffer), -1, on_read);
    }
    else {
        fprintf(stderr, "error opening file: %s\n", uv_strerror(req->result));
    }
    uv_fs_req_cleanup(req);
}
#endif

typedef struct {
  uv_write_t request;
  uv_buf_t buffer;
} w3s_write_request;

uv_loop_t *loop;

void alloc_buffer(uv_handle_t *handle, size_t suggested_size, uv_buf_t* buf) {
    printf("Alloc Buffer with %d\n", (int) suggested_size);
    buf->base = malloc(suggested_size);
    buf->len = suggested_size;
}

void free_w3s_write_request(uv_write_t *req) {
    w3s_write_request *wr = (w3s_write_request *) req;
    free(wr->buffer.base);
    free(wr);
}

void on_close(uv_handle_t* peer) {
    free(peer);
}

void echo_shutdown(uv_shutdown_t* request, int status) {
    uv_close((uv_handle_t*)request->handle, on_close);
    free(request);
}

void echo_write(uv_write_t *req, int status) {

    free_w3s_write_request(req);

    if (status == 0) {
        return;
    }

    fprintf(stderr, "Write error: %s\n", uv_strerror(status));

}

void echo_read(uv_stream_t *client, ssize_t nread, const uv_buf_t* buffer) {
    
    w3s_write_request *wr;
    uv_shutdown_t *shutdown_request;

    /**
     * We received an error or an EOF.
     */
    if (nread < 0) {

        if (nread != UV_EOF) {
            fprintf(stderr, "Read error: %s\n", uv_strerror(nread));
        }

        if (buffer->base) {
            free(buffer->base);
        }

        fprintf(stderr, "Close\n");

        shutdown_request = (uv_shutdown_t*) malloc(sizeof *shutdown_request);
        uv_shutdown(shutdown_request, client, echo_shutdown);

        return;
    }

    /**
     * Nothing to read...
     */
    if(nread == 0) {

        if(buffer->base) {
            free(buffer->base);
        }
        return;
    }

    wr = (w3s_write_request *) malloc(sizeof *wr);

    wr->buffer = uv_buf_init(buffer->base, nread);

    uv_write(&wr->request, client, &wr->buffer, 1, echo_write);

}

void on_new_connection(uv_stream_t *server, int status) {

    uv_tcp_t *client;
    int accept;

    if (status != 0) {
        fprintf(stderr, "Error on new connection: %s\n", uv_strerror(status));
        return;
    }

    client = (uv_tcp_t*) malloc(sizeof(uv_tcp_t));
    uv_tcp_init(loop, client);

    accept = uv_accept(server, (uv_stream_t*) client);

    if(accept == 0) {

        uv_read_start((uv_stream_t*) client, alloc_buffer, echo_read);

    } else {

        fprintf(stderr, "Error on new connection: %s\n", uv_strerror(accept));
        uv_close((uv_handle_t*) client, NULL);

    }

}

int main(int argc, char **argv) {
    if(argc > 1) {

        struct sockaddr_in bind_addr;
        uv_tcp_t server;
        int listen;
        int status;

        loop = uv_default_loop();

        uv_tcp_init(loop, &server);

        uv_ip4_addr("0.0.0.0", atoi(argv[1]), &bind_addr);

        uv_tcp_bind(&server, (const struct sockaddr*) &bind_addr);

        listen = uv_listen((uv_stream_t*) &server, SOMAXCONN, on_new_connection);

        if(listen) {
            fprintf(stderr, "Listen error %s\n", uv_strerror(listen));
            return 2;
        }

        /* uv_fs_open(loop, &open_req, argv[1], O_RDONLY, 0, on_open); */

        return uv_run(loop, UV_RUN_DEFAULT);

    } else {
        fprintf(stderr, "Usage: %s file\n", argv[0]);
        return 1;
    }
}