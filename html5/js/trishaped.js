$(document).ready(function() {

	var instance = __sigma_init();
	var layout = $("#sigma");
	var context = {
		'enableForceAtlas2': true,
		'timeForceAtlas2': 3000,
	}

	__sigma_event(layout, instance, context);
	__sigma_engine(layout, instance, context);

});

function __sigma_engine(layout, instance, context) {
	__fa2_engine('start', instance, context);
}

function __sigma_event(layout, instance, context) {

	layout.mouseenter(function() {
		__fa2_engine('pause', instance, context);
	});
	layout.mouseleave(function() {
		__fa2_engine('resume', instance, context);
	});
	layout.dblclick(function() {
		console.log("Rescale Graph");
		instance.position(0, 0, 1).draw();
	});

	instance.bind('overnodes', function(event) {

		var nodes = event.content;
		var neighbors = {};

		instance.iterEdges(function(e) {

			if(nodes.indexOf(e.source) >= 0 || nodes.indexOf(e.target) >= 0) {
				neighbors[e.source] = 1;
				neighbors[e.target] = 1;
			}

		}).iterNodes(function(n) {
			
			if(!neighbors[n.id]) {
				n.hidden = 1;
			} else {
				n.hidden = 0;
			}

		}).draw(2,2,2);

	});

	instance.bind('outnodes', function() {

		instance.iterEdges(function(e) {
			e.hidden = 0;
		}).iterNodes(function(n) {
			n.hidden = 0;
		}).draw(2,2,2);

	});

}

function __sigma_init() {

	var root = document.getElementById('sigma');
	var instance = sigma.init(root).drawingProperties({
		defaultLabelColor: '#fff',
		defaultLabelSize: 14,
		defaultLabelBGColor: '#fff',
		defaultLabelHoverColor: '#000',
		labelThreshold: 6,
		defaultEdgeType: 'line',
		defaultEdgeArrow: 'target'
	}).graphProperties({
		minNodeSize: 6,
		maxNodeSize: 15,
		minEdgeSize: 1,
		maxEdgeSize: 7
	}).mouseProperties({
		maxRatio: 64
	});

	instance.addNode('hello', __node('Hello')).addNode('world', __node('World !')).addEdge('hello_world','hello','world');
	instance.addNode('trishaped', __node('Trishaped')).addEdge('hello_trishaped', 'hello', 'trishaped');
	instance.addNode('rdf', __node('RDF')).addEdge('trishaped_rdf', 'trishaped', 'rdf');
	instance.addNode('alexandre', __node('Alexandre')).addEdge('0', 'alexandre', 'trishaped');
	instance.addNode('arnaud', __node('Arnaud')).addEdge('1', 'arnaud', 'trishaped');
	instance.addNode('ludovic', __node('Ludovic')).addEdge('2', 'ludovic', 'trishaped');
	instance.addNode('remy', __node('Rémy')).addEdge('3', 'remy', 'trishaped');
	instance.addNode('thomas', __node('Thomas')).addEdge('4', 'thomas', 'trishaped');

	// Resize Node
	instance.iterNodes(function(node) {
		node.size = (node.outDegree + node.inDegree) / 2;
	});

	// Resize Edge
	instance.iterEdges(function(edge) {

		var source = instance.getNodes(edge.source);
		var target = instance.getNodes(edge.target);

		edge.size = (source.size + target.size) / 2;

	});

	// Draw the graph
	instance.draw();

	return instance;
}

function __node(label) {
	return {
		label: label,
		color: __color(),
		x: __position(),
		y: __position()
	};
}

function __color() {
	return '#'+Math.floor(Math.random()*16777215).toString(16);
}

function __position() {
	return Math.random();
}

function __fa2_engine(action, instance, context) {

	var message = false;

	switch(action) {
		case 'start': {

			context.enableForceAtlas2 = true;
			instance.startForceAtlas2();

			setTimeout(function() {

				__fa2_engine('stop', instance, context);

			}, context.timeForceAtlas2);

			message = true;

			break;
		}
		case 'stop': {

			context.enableForceAtlas2 = false;
			instance.stopForceAtlas2();
			message = true;

			break;
		}
		case 'resume': {

			if(context.enableForceAtlas2) {
				instance.startForceAtlas2();
				message = true;
			}

			break;
		}
		case 'pause': {

			if(context.enableForceAtlas2) {
				instance.stopForceAtlas2();
				message = true;
			}

			break;
		}
		default: {
			throw "Unknown action: "+action;
		}
	}

	if(message) {
		console.log(action[0].toUpperCase() + action.slice(1) + " Force Atlas 2 Algorithm");
	}
}