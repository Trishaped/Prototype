#ifndef STATIC_EXCEPTION_H
#define STATIC_EXCEPTION_H

#include <sstream>

using namespace std;

/**
 * 				Exception class
 *
 * Now you can use stream
 * in your Exception:
 * Exception(Stream() << "Some texte" << some_var << endl);
 */

class Stream {
	ostringstream stream;
public:
	Stream(){}
	template <typename T>
	Stream& operator<<( T const& value ){
		stream << value;
	    return *this;
	}
	string str(){ return stream.str(); }
};


class Exception : public exception
{
	public:
	  Exception(string msg="") : msg(msg) {}
	  Exception(Stream& msg) : msg(msg.str()) {}
	  ~Exception() throw() {}
	  const char* what() const throw() { return msg.c_str(); }

	private:
	  string msg;
};



#endif
