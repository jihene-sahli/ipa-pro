joint.shapes.custom = {};
iw.group.hierarchyType = {};
iw.group.groupList = {};
iw.group.linkList = {};
iw.group.paperEl = '#paper';
iw.group.draw = {};
iw.group.draw.member = {};
iw.hierarchy = {};
iw.hierarchy.draw = {};
iw.group.draw.member.width = 300;
iw.group.draw.member.height = 80;
iw.group.draw.paperWidth = jQuery(iw.group.paperEl).width();
iw.group.draw.initX = iw.group.draw.paperWidth / 2 - iw.group.draw.member.width / 2;
iw.group.draw.initY = 10;
iw.group.draw.spacingX = 100;
iw.group.draw.spacingY = 30;
iw.group.draw.nbrByPaper = Math.floor(iw.group.draw.paperWidth / (iw.group.draw.spacingX + iw.group.draw.member.width));

iw.hierarchy.draw.initX = 10;
iw.hierarchy.draw.initY = 60;

iw.hierarchy.draw.getX = function (index) {
    return iw.hierarchy.draw.initX + iw.group.draw.spacingX + iw.group.draw.member.width;
};
iw.hierarchy.draw.getY = function (index) {
    return iw.hierarchy.draw.initY + (iw.group.draw.member.height + iw.group.draw.spacingY) * index;
};
iw.hierarchy.draw.getLinkY = function (index) {
    return iw.hierarchy.draw.getY(index) + Math.floor(iw.group.draw.member.height / 2);
};
iw.hierarchy.draw.getMiddleY = function (nbr) {
    return iw.hierarchy.draw.initY + Math.floor((iw.group.draw.member.height + iw.group.draw.spacingY) * (nbr - 2) / 2);
};
iw.hierarchy.draw.getMiddleLinkY = function (nbr) {
    return iw.hierarchy.draw.getMiddleY(nbr) + Math.floor(iw.group.draw.member.height / 2);
};
iw.hierarchy.draw.getMiddleLinkX = function (nbr) {
    return iw.hierarchy.draw.initX + iw.group.draw.member.width + Math.floor(iw.group.draw.spacingX / 2);
};
iw.group.draw.colors = ["#26C6DA", "#FB8C00", "#F1C40F", "#2ECC71", "#3498DB", "#ffb46c", "#ff8257", "#806af4", "#1dd1c7"];
iw.group.draw.bgColors = ["#008e09", "gray", "#333", "#333", "#333", "#333", "#333"];

iw.group.draw.getX = function (index) {

    return iw.group.draw.spacingX + ((iw.group.draw.member.width
            + iw.group.draw.spacingX) * (index % iw.group.draw.nbrByPaper));
};
iw.group.draw.getY = function (index) {
    return (Math.floor(((iw.group.draw.spacingX + (iw.group.draw.member.width
            + iw.group.draw.spacingX) * index) / iw.group.draw.paperWidth)) + 1) * (iw.group.draw.member.height + iw.group.draw.spacingY);
};

iw.group.draw.getTreeX = function (index, childrenNbr, parentChildrenNbr) {

    return iw.group.draw.spacingX + ((iw.group.draw.member.width
            + iw.group.draw.spacingX) * (index % iw.group.draw.nbrByPaper));
};

iw.group.hierarchyGraph = new joint.dia.Graph;
joint.shapes.custom.link = joint.shapes.org.Arrow.extend({
    defaults: joint.util.deepSupplement({
        type: 'custom.link'
    }, joint.shapes.org.Arrow.prototype.defaults)
});
;

joint.shapes.custom.linkView = joint.dia.LinkView.extend({
    pointerdblclick: function () {
        if (this.model.attr('.connection').hasOwnProperty('stroke-dasharray')) {
            this.model.removeAttr('.connection');
            this.model.attr({'.connection': {stroke: "#585858", 'stroke-width': 3}});
        } else
            this.model.attr({'.connection': {stroke: 'blue', 'stroke-dasharray': '5 2'}});
    }
});

iw.group.paper = new joint.dia.Paper({
    el: $(iw.group.paperEl),
    width: '100%',
    height: 1024,
    gridSize: 1,
    model: iw.group.hierarchyGraph,
    perpendicularLinks: true
});

iw.group.member = function (x, y, rank, name, image, background, border) {

    var cell = new joint.shapes.org.Member({
        position: {x: x, y: y},
        size: {width: iw.group.draw.member.width, height: iw.group.draw.member.height},
        outPorts: ['out'],
        attrs: {
            '.card': {fill: background, stroke: border},
            image: {'xlink:href': image},
            '.rank': {text: rank}, '.name': {text: name},
            '.outPorts circle': {fill: background}
        }
    });
    iw.group.hierarchyGraph.addCell(cell);
    return cell;
};

iw.group.link = function link(source, target, breakpoints) {

    var cell = new joint.shapes.custom.link({
        source: {id: source.id},
        target: {id: target.id},
        vertices: breakpoints,
        attrs: {'.marker-target': {d: 'M 10 0 L 0 5 L 10 10 z'}}
    });
    iw.group.hierarchyGraph.addCell(cell);
    return cell;
};
iw.group.paper.on('cell:pointerup', function (cellView, evt, x, y) {

    // Find the first element below that is not a link nor the dragged element itself.
    var elementBelow = iw.group.hierarchyGraph.get('cells').find(function (cell) {
        if (cell instanceof joint.dia.Link)
            return false; // Not interested in links.
        if (cell.id === cellView.model.id)
            return false; // The same element as the dropped one.
        if (cell.getBBox().containsPoint(g.point(x, y))) {
            return true;
        }
        return false;
    });

    // If the two elements are connected already, don't
    // connect them again (this is application specific though).
    if (elementBelow && !_.contains(iw.group.hierarchyGraph.getNeighbors(elementBelow), cellView.model)) {
        if (elementBelow.attr('.rank').text !== iw.sec.roleAdmin && cellView.model.attr('.rank').text !== iw.sec.roleAdmin) {
            iw.group.hierarchyGraph.addCell(iw.group.link(elementBelow, cellView.model, []));/*(new joint.shapes.org.Arrow({
             source: {id: cellView.model.id}, target: {id: elementBelow.id},
             attrs: {'.marker-source': {d: 'M 10 0 L 0 5 L 10 10 z'}}
             }));*/
            // Move the element a bit to the side.
            cellView.model.translate(300, 20);
        }
    }
});
iw.group.getLinks = function () {
    var links = [];
    iw.group.hierarchyGraph.getLinks().forEach(function (element) {
        var link = iw.group.hierarchyGraph.getCell(element.id);
        var source = iw.group.hierarchyGraph.getCell(link.get('source').id);
        var destination = iw.group.hierarchyGraph.getCell(link.get('target').id);
        var linkType = iw.group.hierarchyType.normal;
        if (link.attr('.connection').hasOwnProperty('stroke-dasharray')) {
            linkType = iw.group.hierarchyType.functional;
        }
        links.push({source: source.attr('.rank').text, target: destination.attr('.rank').text, type: linkType});
    });
    return links;
};

