var graph   = new joint.dia.Graph();
var paper   = new joint.dia.Paper({
    el                  : $('#paper'),
    width               : 1400,
    height              : 400,
    gridSize            : 10,
    perpendicularLinks  : true,
    model               : graph,
    interactive         : false

});
var SelectedElement;

paper.on('cell:pointerdown',
    function(cellView, evt, x, y) {
      //console.log(cellView.model.id);
      SelectedElement = cellView.model;
      elemType    = cellView.model.attr('elem/type');
      $('#pd-details').css('display','none');
      if(elemType != 'link' && elemType != 'btn') {
        elemid      = cellView.model.attr('elem/id');
        elemName    = cellView.model.attr('elem/name');
        elemActive  = cellView.model.attr('elem/active');
        $('#pd-details').css('display','');
        $('#pd-details-name').html('');
        $('#pd-details-name').html(elemName);
        $('#pd-details-active').val(elemActive);
        $('a[data-toggle="pd-details-active"]').not('[data-title="'+elemActive+'"]').removeClass('active').addClass('notActive');
        $('a[data-toggle="pd-details-active"][data-title="'+elemActive+'"]').removeClass('notActive').addClass('active');
      }
    }
);

$('#radioBtn a').on('click', function(){
    var sel = $(this).data('title');
    var tog = $(this).data('toggle');
    $('#'+tog).prop('value', sel);

    $('a[data-toggle="'+tog+'"]').not('[data-title="'+sel+'"]').removeClass('active').addClass('notActive');
    $('a[data-toggle="'+tog+'"][data-title="'+sel+'"]').removeClass('notActive').addClass('active');

    //SelectedElement.remove();
    AllElements   = graph.getCells();
    SelectedElement.attr('elem/active',sel) ;
    if(SelectedElement.attr('elem/active')==1){
      _.each(AllElements, function(t) {
        console.log(t.attr('elem/id'));
        if (t.attr('elem/id')==SelectedElement.attr('elem/id')+'-okbtn'){
          t.attr('./display', '');
        }
      });

    }else{
      _.each(AllElements, function(t) {
        if (t.attr('elem/id')==SelectedElement.attr('elem/id')+'-okbtn'){
          t.attr('./display', 'none');
        }
      });


    }

})
var pn      = joint.shapes.pn;

//FUNCTIONS SHAPES
var setTransition = function(x, y,text){
  var cell = new pn.Transition({
      position: { x: x, y: y },
      attrs: {
          '.label': { text: text, fill: '#fe854f' },
          '.root' : { fill: '#9586fd', stroke: '#9586fd' }
      }
  });

  graph.addCell(cell);
  return cell;
}

var setPicture  = function (x, y,width,height, options){
  var cell    = new joint.shapes.basic.Image({
                position  : {x : x, y : y},
                size      : {width   : width, height  : height},
                attrs : {
                    image : {
                        "xlink:href" : "img/" + options['image'],
                        width   : width,
                        height  : height
                    },
                    elem: { type: options['type']       , id  : options['id'], name: options['name'], active: options['active']}
                }
              });
  graph.addCell(cell);
  return cell;

}
var setRectangle  = function(x, y, width, height, options) {
    var cell      = new joint.shapes.basic.Rect({
                    position: { x: x, y: y },
                    size    : { width: width, height: height },
                    attrs   : {
                                  rect: { fill: options['fill']       , stroke: options['stroke'] },
                                  text: { text: options['text']       , fill: options['textColor'] },
                                  elem: { type: options['type']       , id  : options['id'], name: options['name'], active: options['active']}
                              }

                  });
    graph.addCell(cell);
    return cell;
};
function link(a, b,breakpoints) {

    return new pn.Link({
        source: { id: a.id, selector: '.root' },
        target: { id: b.id, selector: '.root' },
        vertices  : breakpoints,
        attrs: {
            '.connection': {
                'fill'            : 'none',
                'stroke-linejoin' : 'round',
                'stroke-width'    : '2',
                'stroke'          : '#4b4a67'
            },
            elem: { type: 'link'     }
        }
    });
}


var setPlace  = function (x,y,text,token,options){
    var cell  = new pn.Place({
        position: { x: x, y: y },
                  attrs: {
                      '.label'          : { text: text, fill: '#7c68fc' },
                      '.root'           : { stroke: '#9586fd', 'stroke-width': 3 },
                      '.tokens > circle': { fill : '#7a7e9b' },
                      elem: { type: options['type']       , id  : options['id'], name: options['name']}
                  },
        tokens: token
    });
    graph.addCell(cell);
    return cell;
}


//------------------------------------------------------//

//BUILDING 01
options           = new Array();
options['active'] = 0;
options['image']  = 'building1.png';
options['type']   = 'building';
options['name']   = 'BUILDING 01';
options['id']     = guid();
var pBuilding     = setPicture(30,130,70,70, options);
options['image']  = 'ok.png';
options['id']     = options['id']+'-okbtn';
options['type']   = 'btn';
var pOk           = setPicture(90,120,20,20, options);
if(options['active']==1) pOk.attr('./display', ''); else pOk.attr('./display', 'none');

//BUILDING 02
options['image']  = 'building1.png';
options['name']   = 'BUILDING 02';
options['type']   = 'building';
options['active'] = 0;
options['id']     = guid();
var pBuilding2    = setPicture(600,160,70,70, options);
options['image']  = 'ok.png';
options['id']     = options['id']+'-okbtn';
options['type']   = 'btn';
var pOk           = setPicture(660,150,20,20, options);
if(options['active']==1) pOk.attr('./display', ''); else pOk.attr('./display', 'none');


//PARAB 01
options['image']  = 'parab.png';
options['name']   = 'PARAB 01';
options['type']   = 'parab';
options['active'] = 0;
options['id']     = guid();
var pParab1       = setPicture(1000,50,70,70, options);
options['image']  = 'ok.png';
options['type']   = 'btn';
options['id']     = options['id']+'-okbtn';
var pOk           = setPicture(1060,40,20,20, options);
if(options['active']==1) pOk.attr('./display', ''); else pOk.attr('./display', 'none');

//PARAB 02
options['image']= 'parab.png';
options['name'] = 'PARAB 02';
options['type']   = 'parab';
options['id']     = guid();
var pParab2     = setPicture(1000,260,70,70, options);
options['image']  = 'ok.png';
options['type']   = 'btn';
options['id']     = options['id']+'-okbtn';
var pOk           = setPicture(1060,250,20,20, options);
if(options['active']==1) pOk.attr('./display', ''); else pOk.attr('./display', 'none');

//SATELLITE 01
options['image']  = 'satellite.png';
options['name']   = 'SATEL 01';
options['type']   = 'satel';
options['id']     = guid();
var pSatel        = setPicture(1250,160,70,70, options);
options['image']  = 'ok.png';
options['type']   = 'btn';
options['id']     = options['id']+'-okbtn';
var pOk           = setPicture(1310,150,20,20, options);
if(options['active']==1) pOk.attr('./display', ''); else pOk.attr('./display', 'none');


//RECTANGLE 01
options['fill']       = '#ffffff';
options['textColor']  = 'black';
options['type']       = 'rect';
options['text']       = 'FO 59724';
options['name']       = 'FO 59724';
options['id']         = guid();
var pRectangle1       = setRectangle(200,50, 100, 50, options);
options['image']      = 'ok.png';
options['type']       = 'btn';
options['id']         = options['id']+'-okbtn';
var pOk               = setPicture(290,40,20,20, options);
if(options['active']==1) pOk.attr('./display', ''); else pOk.attr('./display', 'none');


//RECTANGLE 02
options['text']   = 'MUX R4 Detail';
options['name']   = 'MUX R4 Detail';
options['id']     = guid();
options['type']       = 'rect';
var pRectangle2   = setRectangle(200,260, 100, 50, options);
options['image']  = 'ok.png';
options['type']   = 'btn';
options['id']     = options['id']+'-okbtn';
var pOk           = setPicture(290,250,20,20, options);
if(options['active']==1) pOk.attr('./display', ''); else pOk.attr('./display', 'none');

//RECTANGLE 03
options['stroke']     = 'none';
options['textColor']  = 'red';
options['text']       = 'FTR&D';
options['name']       = 'FTR&D';
options['type']       = 'rect';
options['id']         = guid();
var pRectangle3       = setRectangle(400,160, 60, 25, options);
options['image']      = 'ok.png';
options['type']       = 'btn';
options['id']         = options['id']+'-okbtn';
var pOk               = setPicture(450,150,20,20, options);
if(options['active']==1) pOk.attr('./display', ''); else pOk.attr('./display', 'none');

//RECTANGLE 04
options['text']       = 'MULTI4';
options['name']       = 'MULTI4';
options['type']       = 'rect';
options['id']         = guid();
var pRectangle4       = setRectangle(800,160, 60, 25, options);
options['image']      = 'ok.png';
options['type']       = 'btn';
options['id']         = options['id']+'-okbtn';
var pOk               = setPicture(850,150,20,20, options);
if(options['active']==1) pOk.attr('./display', ''); else pOk.attr('./display', 'none');

/*
//PLACES
//var pReady      = setPlace(140,50   , 'ready'       ,1);
//var pIdle       = setPlace(140,260  , 'idle'        ,2);
//var buffer      = setPlace(350,160  , 'buffer'      ,20);
//var cAccepted   = setPlace(550,50   , 'accepted'    ,1);
//var cReady      = setPlace(560,260  , 'ready'       ,1);

//TRANSITIONS
//var pProduce  = setTransition(50,160, 'produce');
//var pSend     = setTransition(270,160, 'send');
//var cAccept   = setTransition(470,160, 'accept');
//var cConsume  = setTransition(680,160, 'consume');
*/

//LINKS
graph.addCell([
    link(pBuilding    , pRectangle1 ,  [{x:70,y:80}]),
    link(pBuilding    , pRectangle2 ,  [{x:70,y:280}]),

    link(pRectangle1 , pRectangle3 ,  [{x:430,y:80}]),
    link(pRectangle2 , pRectangle3 ,  [{x:430,y:280}]),

    link(pRectangle3  , pBuilding2),
    link(pBuilding2   , pRectangle4),

    link(pRectangle4  , pParab1   ,  [{x:830,y:80}]),
    link(pRectangle4  , pParab2   ,  [{x:830,y:280}]),


    link(pParab1      , pSatel    ,  [{x:1300,y:80}]),
    link(pParab2      , pSatel    ,  [{x:1300,y:280}])
]);



function fireTransition(t, sec, inbound, outbound) {

    var inbound       = graph.getConnectedLinks(t, { inbound: inbound });
    var outbound      = graph.getConnectedLinks(t, { outbound: outbound });

    var placesBefore  = _.map(inbound, function(link) {
        return graph.getCell(link.get('source').id);
    });
    var placesAfter   = _.map(outbound, function(link) {
        return graph.getCell(link.get('target').id);
    });

    var isFirable     = true;
    _.each(placesBefore, function(p) { if (p.get('tokens') === 0) isFirable = false; });



    if (isFirable) {
        _.each(placesBefore, function(p) {
            // Let the execution finish before adjusting the value of tokens. So that we can loop over all transitions
            // and call fireTransition() on the original number of tokens.
            _.defer(function() { p.set('tokens', p.get('tokens') - 1); });

            var link  = _.find(inbound, function(l) { return l.get('source').id === p.id; });
            paper.findViewByModel(link).sendToken(V('circle', { r: 5, fill: '#feb662' }).node, sec * 1000);

            });

            _.each(placesAfter, function(p) {
            var link  = _.find(outbound, function(l) { return l.get('target').id === p.id; });
                paper.findViewByModel(link).sendToken(V('circle', { r: 5, fill: '#feb662' }).node, sec * 1000, function() {
                        p.set('tokens', p.get('tokens') + 1);
                });
            });
    }

}

function simulate() {
    var transitions = [
                        [pBuilding],
                        [pRectangle1, pRectangle2],
                        [pRectangle3],
                        [pBuilding2],
                        [pRectangle4],
                        [pParab1, pParab2]
                        ];

    /*
    _.each(transitions[cpt]s, function(t)     {
        var mathRando = Math.random();
        mathRando     = 0.6;
        if (mathRando < 0.7) fireTransition(t, 1,false, true);
      });
    */


    /*
    return setInterval(function() {
        _.each(transitions, function(t) { if (Math.random() < 0.7) fireTransition(t, 1); });
    }, 2000);
    */

    var cpt = 0;
    return setInterval(function() {
      _.each(transitions[cpt], function(t) {fireTransition(t, 1,false,true);});
      cpt++;
      if(cpt == transitions.length) cpt =0;

    }, 1000);
}

function stopSimulation(simulationId) {
    clearInterval(simulationId);
}

function guid() {
  function s4() {
    return Math.floor((1 + Math.random()) * 0x10000)
      .toString(16)
      .substring(1);
  }
  return s4() + s4() + '-' + s4() + '-' + s4() + '-' +
    s4() + '-' + s4() + s4() + s4();
}


var simulationId = simulate();

/*
var pIdle   = pReady.clone().attr({
    '.label': { text: 'idle' }
}).position(140, 260).set('tokens', 2);

var buffer  = pReady.clone().attr({
    '.label': { text: 'buffer' },
    '.alot > text': {
        fill: '#fe854c',
        'font-family': 'Courier New',
        'font-size': 20,
        'font-weight': 'bold',
        'ref-x': 0.5,
        'ref-y': 0.5,
        'y-alignment': -0.5
    }
}).position(350, 160).set('tokens', 12);

var cAccepted = pReady.clone().attr({
    '.label': { text: 'accepted' }
}).position(550, 50).set('tokens', 1);

var cReady = pReady.clone().attr({
    '.label': { text: 'accepted' }
}).position(560, 260).set('ready', 3);



var pSend = pProduce.clone().attr({
    '.label': { text: 'send' }
}).position(270, 160);

var cAccept = pProduce.clone().attr({
    '.label': { text: 'accept' }
}).position(470, 160);

var cConsume = pProduce.clone().attr({
    '.label': { text: 'consume' }
}).position(680, 160);
*/
