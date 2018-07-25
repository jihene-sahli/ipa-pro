var graph = new joint.dia.Graph();

var paper = new joint.dia.Paper({
    el                  : $('#paper'),
    width               : 1400,
    height              : 600,
    gridSize            : 1,
    model               : graph,
    perpendicularLinks  : true,
    interactive         : false,
    restrictTranslate   : true
});


var rect1      = function(x, y, width, height, options) {
    var cell   = new joint.shapes.basic.Rect({
                    position: { x: x, y: y },
                    size    : { width: width, height: height },
                    attrs   : {
                                  rect: { fill: options['fill']       , stroke: options['stroke'] },
                                  text: { text: options['text']       , fill: options['textColor'] },
                                  elem: { type: options['type']       , id  : options['id']}
                              }

                  });
    graph.addCell(cell);
    return cell;
};

var picture1  = function (x, y,width,height, options){
  var cell    = new joint.shapes.basic.Image({
                position  : {x : x, y : y},
                size      : {width   : width, height  : height},
                attrs : {
                    image : {
                        "xlink:href" : "img/" + options['image'],
                        width   : width,
                        height  : height
                    }
                }
              });
  graph.addCell(cell);
  return cell;

}

function link(source, target, breakpoints) {
    var cell = new joint.dia.Link({
        source    : { id: source.id },
        target    : { id: target.id },
        vertices  : breakpoints,

        attrs     : {
                      '.connection': {
                            'fill'            : '#4b4a67',
                            'stroke-linejoin' : 'round',
                            'stroke-width'    : '2',
                            'stroke'          : '#4b4a67'
                          },

                        '.marker-target': { fill: '#4b4a67', stroke: '#4b4a67', d: 'M 10 0 L 0 5 L 10 10 z' }
                    }

    });
    graph.addCell(cell);
    return cell;
}
//BUILDING 1
options               = new Array();
options['type']       = 'Building';
options['id']         = '54dHdfs25TR25';
options['image']      = 'building1.png';
var build1            = picture1(0,0,100,100, options);
//RECT 01
options               = new Array();
options['fill']       = '#ffffff';
options['text']       = 'FO 59724';
options['textColor']  = 'black';
options['type']       = 'compenent';
options['id']         = '54dHyk25TR25';
var rectangle1        = rect1(200,30, 100, 50, options);
//RECT 02
options               = new Array();
options['fill']       = '#ffffff';
options['text']       = 'MUX R4 Detail';
options['textColor']  = 'black';
options['type']       = 'compenent';
options['id']         = '54dHyk25TR25';
var rectangle2        = rect1(400,00, 120, 350, options);
//RECT 03
options               = new Array();
options['fill']       = '#ffffff';
options['stroke']     = 'none';
options['text']       = 'MULTI4';
options['textColor']  = 'red';
options['type']       = 'compenent';
options['id']         = '54dHyk25TR25';
var rectangle3        = rect1(600,70, 60, 25, options);
//RECT 04
options               = new Array();
options['fill']       = '#ffffff';
options['stroke']     = 'none';
options['text']       = 'FTR&D';
options['textColor']  = 'red';
options['type']       = 'compenent';
options['id']         = '54dHyk25TR25';
var rectangle4        = rect1(600,170, 60, 25, options);

//RECT 05
options               = new Array();
options['fill']       = '#ffffff';
options['text']       = 'STA Detail';
options['textColor']  = 'black';
options['type']       = 'compenent';
options['id']         = '54dHyk25TR25';
var rectangle5        = rect1(800,90, 100, 75, options);
//PARAB 1
options               = new Array();
options['type']       = 'parab';
options['id']         = '54dHdfs25TR25';
options['image']      = 'parab.png';
var parab1            = picture1(1000,90,50,50, options);
//SAT 1
options               = new Array();
options['type']       = 'parab';
options['id']         = '54dHdfs25TR25';
options['image']      = 'satellite.png';
var sat1              = picture1(1200,30,50,50, options);


link(build1, rectangle1);
link(rectangle1, rectangle2);
link(rectangle2, rectangle3);
link(rectangle2, rectangle4);
link(rectangle3, rectangle5);
link(rectangle4, rectangle5);
link(rectangle5, parab1);
link(parab1, sat1, [{x:1150,y:60}, {x:1140,y:90}]);


/*
var homer = building(90,200,'VP Marketing', 'Homer Simpson', 'male.png', '#7c68fd', '#f1f1f1');
var marge = building(300,200,'VP Sales', 'Marge Simpson', 'female.png', '#7c68fd', '#f1f1f1');
var lisa = building(500,200,'VP Production' , 'Lisa Simpson', 'female.png', '#7c68fd', '#f1f1f1');
var maggie = building(400,350,'Manager', 'Maggie Simpson', 'female.png', '#feb563');
var lenny = building(190,350,'Manager', 'Lenny Leonard', 'male.png', '#feb563');
var carl = building(190,500,'Manager', 'Carl Carlson', 'male.png', '#feb563');



link(bart, marge, [{x: 385, y: 180}]);
link(bart, homer, [{x: 385, y: 180}, {x: 175, y: 180}]);
link(bart, lisa, [{x: 385, y: 180}, {x: 585, y: 180}]);
link(homer, lenny, [{x:175 , y: 380}]);
link(homer, carl, [{x:175 , y: 530}]);
link(marge, maggie, [{x:385 , y: 380}]);
*/
