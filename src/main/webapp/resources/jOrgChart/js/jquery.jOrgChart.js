// jQuery Plugin
(function($) {

  var cx = 0;
  var loopflag = false;
  $.fn.jOrgChart = function(options) {

    var opts = $.extend({}, $.fn.jOrgChart.defaults, options);
    var $appendTo = $(opts.chartElement);

    // build the tree
    $this = $(this);

    var $container = $("<div class='" + opts.chartClass + "'/>");
    if ($this.is("ul")) {
      if (opts.rowcolor) {
        //add color to row wise 
        $this.find("li").each(function() {
          classList = $(this).attr('class').split(/\s+/);
          $.each(classList, function(index, item) {
            if (item != "temp" && item != "node" && item != "child" && item != "ui-draggable" && item != "ui-droppable" && !/^unic\d+$/i.test(item)) {
              re = item;
            }
          });
          if (re != null) {
            $(this).removeClass(re)
          }

          var col = $(this).parents('li').length;

          if (col == 0) {
            $(this).addClass("nrow");
          } else if (col == 1) {
            $(this).addClass("firow");
          } else if (col == 2) {
            $(this).addClass("serow");
          } else if (col == 3) {
            $(this).addClass("throw");
          } else if (col == 4) {
            $(this).addClass("forow");
          } else if (col == 5) {
            $(this).addClass("firow");
          } else if (col == 6) {
            $(this).addClass("sirow");
          } else {
            $(this).addClass("norow");
          }

        });
      }
      $this.find("li.root").each(function() {
        buildNode($(this), $container, 0, opts);
      })
    } else if ($this.is("li")) {
      buildNode($this, $container, 0, opts);
    }
    $appendTo.append($container);

    // add drag and drop if enabled
    if (opts.dragAndDrop) {
      var $divNode = $('.node:not(.temp)').not(".disabled, #org .node");
      var $nodeParts = $divNode.not(".child");
      $divNode.draggable({
        cursor: 'move',
        distance: 40,
        helper: 'clone',
        opacity: 0.8,
        revert: 'invalid',
        revertDuration: 100,
        snap: 'div.node.expanded',
        snapMode: 'inner',
        stack: 'div.node',
      });


      $nodeParts.droppable({
        accept: '.node',
        activeClass: 'drag-active',
        hoverClass: 'drop-hover'
      });


      var $nodeCU = $("div.node.child");
      $nodeCU.droppable({
        accept: '.child',
        activeClass: 'drag-active',
        hoverClass: 'drop-hover'
      });

      // Drag start event handler for nodes

      $divNode.bind("dragstart", function handleDragStart(event, ui) {
        var start_event = ui.helper
        var sourceNode = $(this);
        if (start_event.is("div")) {
          sourceNode.parentsUntil('.node-container').find('*').filter('.node').droppable('disable');
        } else if (start_event.is("li")) {
          sourceNode.find('*').filter('.node').droppable('disable');
        }
			$(opts.chartElement).children().find(":not('.direction')").droppable('disable');
      });


      // Drag stop event handler for nodes
      $divNode.bind("dragstop", function handleDragStop(event, ui) {

        //refresh side bar
        var sideLi = $("#upload-chart").html();
        $("#upload-chart").empty();
        $("#upload-chart").append(sideLi);
        if ($("#upload-chart li:last-child").hasClass("ui-draggable-dragging")) {
          $("#upload-chart li:last-child").remove();
        }

        //remove list from side bar when added to org-chart
        if (removeside_node != "") {
          $("#upload-chart #" + removeside_node).remove();
          removeside_node = "";
        }

        /* reload the plugin */
        $(opts.chartElement).children().remove();
        $this.jOrgChart(opts);
        cutomdata(opts.chartElement);
      });


      // Drop event handler for nodes
      var removeside_node = ""
      $divNode.bind("drop", function handleDropEvent(event, ui) {

        var targetID = $(this).data("tree-node");
        var targetLi = $this.find("li").filter(function() {
          return $(this).data("tree-node") === targetID;
        });
        var targetUl = targetLi.children('ul');

        var sourceID = ui.draggable.data("tree-node");
        if (sourceID == null) {
          var lilength = $this.find("li").length;
          var sourceLi = ui.draggable.clone();
          sourceLi.addClass('unic' + (sourceLi.attr("id")));
          removeside_node = ui.draggable.attr("id");
          //sourceLi.addClass("item");

        } else {
          var sourceLi = $this.find("li").filter(function() {
            return $(this).data("tree-node") === sourceID;
          });
          var sourceUl = sourceLi.parent('ul');
          //Removes any empty lists
          if (sourceUl.children().length === 0) {
            sourceUl.remove();
          }
        }

        sourceLi.removeClass("node").removeClass("ui-draggable")
		sourceLi.find('div').find('.boss').remove();

        if (targetUl.length > 0) {
          targetUl.append(sourceLi);
        } else {
          targetLi.append("<ul></ul>");
          targetLi.children('ul').append(sourceLi);
        }

      }); // handleDropEvent

    } // Drag and drop

    cutomdata(opts.chartElement);
  };

  // Option defaults
  $.fn.jOrgChart.defaults = {
    chartElement: 'chart',
    depth: -1,
    chartClass: "jOrgChart",
    dragAndDrop: false,
    expand: false,
    control: false,
    rowcolor: true
  };

  var nodeCount = 0;

  function removeNode($node, opts, $nodeDiv) {
    if ($nodeDiv.hasClass("temp")) {
      if (click_flag) {
        $node.remove();
        click_flag = true;
        $(opts.chartElement).children().remove();
        $this.jOrgChart(opts);
      }

    }
  }

  // Method that recursively builds the tree
  function buildNode($node, $appendTo, level, opts) {
    var $table = $("<table cellpadding='0' cellspacing='0' border='0'/>");
    var $tbody = $("<tbody/>");

    // Construct the node container(s)
    var $nodeRow = $("<tr/>").addClass("node-cells");
    var $nodeCell = $("<td/>").addClass("node-cell").attr("colspan", 2);
    var $childNodes = $node.children("ul:first").children("li");
    var $nodeDiv;

    if ($childNodes.length > 1) {
      $nodeCell.attr("colspan", $childNodes.length * 2);
    }
    // Draw the node
    // Get the contents - any markup except li and ul allowed
    var $nodeContent = $node.clone()
      .children("ul,li")
      .remove()
      .end()
      .html();

    //Increaments the node count which is used to link the source list and the org chart
    nodeCount++;

    $node.data("tree-node", nodeCount);
    $nodeDiv = $("<div>").addClass("node")
      .data("tree-node", nodeCount)
      .append($nodeContent);


    $nodeDiv.append(
        "<div class='opciones'>" +
        "</div>")
      .mouseenter(function() {
        if ($(this).find("> .details > span").length == 0) {
          var duplicate = $(this).find("> span.label_node").clone();
          $(this).find("> .details").prepend(duplicate);
        }
        $(this).find(".details").toggle().parent().css("z-index", "999");
      }).mouseleave(function() {
        $(this).find(".details").toggle().parent().removeAttr("style");
      });

    var append_text = "<li class='temp'></li>";
    var $list_element = $node.clone()
      .children("ul,li")
      .remove()
      .end();


    // Expand and contract nodes
    if (opts.expand) {
      if ($childNodes.length > 0) {
        $nodeDiv.find(".opciones:eq(0)").closest(".node").append("<span class='exp-col'></span>");
        $nodeDiv.find(".exp-col").click(function() {
          var $this = $(this);
          var $tr = $this.closest("tr");
          if ($tr.hasClass('contracted')) {
            $tr.removeClass('contracted').addClass('expanded');
            $tr.nextAll("tr").css('visibility', '');
            $node.removeClass('collapsed');
          } else {
            $tr.removeClass('expanded').addClass('contracted');
            $tr.nextAll("tr").css('visibility', 'hidden');
            $node.addClass('collapsed');
          }
        });
      }
    }

    $nodeCell.append($nodeDiv);
    $nodeRow.append($nodeCell);
    $tbody.append($nodeRow);

    if ($childNodes.length > 0) {
      // if it can be expanded then change the cursor
      //$nodeDiv.css('cursor','n-resize');

      // recurse until leaves found (-1) or to the level specified
      if (opts.depth == -1 || (level + 1 < opts.depth)) {
        var $downLineRow = $("<tr/>");
        var $downLineCell = $("<td/>").attr("colspan", $childNodes.length * 2);
        $downLineRow.append($downLineCell);

        // draw the connecting line from the parent node to the horizontal line
        $downLine = $("<div></div>").addClass("line down");
        $downLineCell.append($downLine);
        $tbody.append($downLineRow);

        // Draw the horizontal lines
        var $linesRow = $("<tr/>");
        $childNodes.each(function() {
          var $left = $("<td>&nbsp;</td>").addClass("line left top");
          var $right = $("<td>&nbsp;</td>").addClass("line right top");
          $linesRow.append($left).append($right);
        });

        // horizontal line shouldn't extend beyond the first and last child branches
        $linesRow.find("td:first")
          .removeClass("top")
          .end()
          .find("td:last")
          .removeClass("top");

        $tbody.append($linesRow);
        var $childNodesRow = $("<tr/>");
        $childNodes.each(function() {
          var $td = $("<td class='node-container'/>");
          $td.attr("colspan", 2);
          // recurse through children lists and items
          buildNode($(this), $td, level + 1, opts);
          $childNodesRow.append($td);
        });

      }
      $tbody.append($childNodesRow);
    }

    // any classes on the LI element get copied to the relevant node in the tree
    // apart from the special 'collapsed' class, which collapses the sub-tree at this point

    if ($node.attr('class') != undefined) {
      var classList = $node.attr('class').split(/\s+/);
      $.each(classList, function(index, item) {
        if (item == 'collapsed') {

          $nodeRow.nextAll('tr').css('visibility', 'hidden');
          $nodeRow.removeClass('expanded');
          $nodeRow.addClass('contracted');
        } else {
          $nodeDiv.addClass(item);
        }
      });
    }
    if (opts.control) {
      if (!$nodeDiv.hasClass("temp")) {
        
	if ($nodeDiv.hasClass("direction")) {
          $nodeDiv.find(".opciones:eq(0)").append("<span class='edit' href='#fancy_edit_entity'></span>");
          $nodeDiv.find(".opciones:eq(0)").append("<span class='add-entity' href='#fancy_add_entity'></span>");
          $nodeDiv.find(".opciones:eq(0)").append("<span class='add-collaborateur' href='#fancy_add_collaborateur'></span>");
        }
        else{
          $nodeDiv.find(".opciones:eq(0)").append("<span class='edit' href='#fancy_edit_collaborateur'></span>");
          $nodeDiv.find(".opciones:eq(0)").append("<span class='boss-edit'></span>");
        }
        if ($nodeDiv.hasClass("child")) {
          $nodeDiv.find(".opciones:eq(0)").append("<span class='del'></span>");
        }
      }/* else {
        $nodeDiv.find(".opciones:eq(0)").append("<span class='add' href='#fancy_add'></span><span class='del'></span>");
      }*/
    }
    $table.append($tbody);
    $appendTo.append($table);

    /* Prevent trees collapsing if a link inside a node is clicked */
    $nodeDiv.children('a, span').click(function(e) {
      e.stopPropagation();
    });
  }

  function cutomdata(selector) {
    if (loopflag) {
      return;
    }
    loopActive = true;
    var regx = /\w*(row)/;

    $(selector).on("click", ".del", function(e) {
      var nodo = $(this);

      if (!nodo.parent().parent().hasClass("temp")) {
        var nodeDiv = nodo.parent().parent();
        var cu = nodeDiv.find("a").attr("rel");
        var classList = nodeDiv.attr('class').split(/\s+/);
        var del_node;
        $.each(classList, function(index, item) {
          if (item != "temp" && item != "node" && item != "child" && item != "ui-draggable" && item != "ui-droppable" && !regx.test(item)) {
            del_node = item;
          }
        });
        var element = $("li." + del_node + ":not('.temp, #upload-chart li')");
		var d = element.hasClass("direction");
		element.removeAttr("class").addClass("node").addClass("child");
		element.find('*').filter('.boss').remove();
		if(d) element.addClass("direction");
        remChild(element);
        init_tree();
	throw new Error('This is not an error. This is just to abort javascript');
      }
    });
	
	$(selector).on("click", ".boss-edit", function(e) {
		
		
	  var nodo = $(this);
      if (!nodo.parent().parent().hasClass("temp")) {
        var nodeDiv = nodo.parent().parent();
        var cu = nodeDiv.find("a").attr("rel");
        var classList = nodeDiv.attr('class').split(/\s+/);
        var del_node;
        $.each(classList, function(index, item) {
          if (item != "temp" && item != "node" && item != "child" && item != "ui-draggable" && item != "ui-droppable" && !regx.test(item)) {
            del_node = item;
          }
        });
        var element = $("li." + del_node + ":not('.temp, #upload-chart li')");
		var div = element.find('div');
		
		if(div.children().hasClass('boss'))
			div.find('.boss').remove();
		else{
			element.parent().children().each(function() {
				var c = $(this);
				if(!c.hasClass('direction'))
				c.find('.boss').remove();
			});
			div.append("<img src='../../resources/jOrgChart/images/boss.png' class='boss'>");
		}
        init_tree();
        throw new Error('This is not an error. This is just to abort javascript');
      }
	});

    //-- entity || collaborateur editor
    $(selector).on("click", ".edit", function(e) {
      $("#fancy_edit").show();
      var classList = $(this).parent().parent().attr('class').split(/\s+/);
      var tipo_n;
      var del_node
      $.each(classList, function(index, item) {
        if (item != "temp" && item != "node" && item != "child" && item != "ui-draggable" && item != "ui-droppable" && !regx.test(item)) {
          del_node = item;
        }
        if (item == "root" || item == "child") {
          tipo_n = item;
        }
      });
      node_to_edit = $("li." + del_node + ":not('.temp')");
      $("#edit_node_entity").off("click").on("click", function(e) {
        e.preventDefault();
        //modify li and refresh tree
        var edit_field = $("#edit_node_name");
        var texto = edit_field.val();
        if(texto.length != 0)
            node_to_edit.find("> .label_node:eq(0) > a").text(texto);
        edit_field.val("");
        hideBox();
        init_tree();
      });
      
      $("#edit_node_collaborateur").off("click").on("click", function(e) {
        e.preventDefault();
        //modify li and refresh tree
        var edit_firstname = $("#edit_node_firstname");
        var edit_lastname = $("#edit_node_lastname");
        var edit_poste = $("#edit_node_poste");
        var edit_act_id_user = $("#edit_node_user");
        var edit_create_act_id_user = $("#edit_node_create_user");
        var firstname = edit_firstname.val();
        var lastname = edit_lastname.val();
        if(firstname.length != 0)
            node_to_edit.find("> .label_node:eq(0) > a").find('.firstName').text(firstname);
        if(lastname.length != 0)
            node_to_edit.find("> .label_node:eq(0) > a").find('.lastName').text(lastname);
        node_to_edit.find("> .label_node:eq(0) > i").text(edit_poste.find('option:selected').text());
        node_to_edit.find(".poste").val(edit_poste.val());
        node_to_edit.find(".actIdUser").val(edit_act_id_user.val());
        node_to_edit.find(".createActIdUser").val(edit_create_act_id_user.is(':checked'));
        edit_firstname.val("");
        edit_lastname.val("");
        hideBox();
        init_tree();
      });
      
      if($(this).parent().parent().hasClass("direction")){
        $("#edit_node_name").val(node_to_edit.find("> .label_node:eq(0) > a").text());
        $("#fancy_edit_entity").show();
        
      }else{
        $("#edit_node_firstname").val(node_to_edit.find("> .label_node:eq(0) > a").find('.firstName').text());
        $("#edit_node_lastname").val(node_to_edit.find("> .label_node:eq(0) > a").find('.lastName').text());
        $("#edit_node_poste").val(node_to_edit.find(".poste").val());
        $("#edit_node_user").val(node_to_edit.find(".actIdUser").val());
        if(node_to_edit.find(".createActIdUser").val() == "true")
            $("#edit_node_create_user").prop( "checked", true);
        else
            $("#edit_node_create_user").prop( "checked", false);
        $("#fancy_edit_collaborateur").show();
      }
      throw new Error('This is not an error. This is just to abort javascript');
    });


    //Add entity

    $(selector).on("click", ".add-entity", function() {
      $("#fancy_add i").show();
      var classList = $(this).parent().parent().attr('class').split(/\s+/);
      var add_to_node;
      $.each(classList, function(index, item) {
        if (item != "temp" && item != "node" && item != "child" && item != "ui-draggable" && item != "ui-droppable" && !regx.test(item)) {
          add_to_node = item;
          return;
        }
      });
      $("#add_node_entity").off("click").on("click", function(e) {
        e.preventDefault();

        var tipo_nodo = "";
        var text_field = $("#new_node_name");
        var texto = text_field.val();
        text_field.val("");
        var $node = $("li." + add_to_node + ":not('.temp')");
        var childs = $("#org li").size() + $("#upload-chart li").size() + 1;
        tipo_nodo += "direction child unicd-new" + childs;
        var append_text = "<li class='" + tipo_nodo + "' id='d-new" + childs + "'>";
        append_text += "<span class='label_node'><a href='' onclick='return false;'>" + texto + "</a><br><i class='post'>&nbsp;</i></span>";
        append_text += "</li>";
        if ($node.find("ul").size() == 0) {
          append_text = "<ul>" + append_text + "</ul>";
          $node.append(append_text);
        } else {
          $node.find("ul:eq(0)").append(append_text);
        }

        hideBox();
        init_tree();
        scroll()
      });
      $("#fancy_add_entity").show();
      throw new Error('This is not an error. This is just to abort javascript');
    });
    
    //Add collaborateur

    $(selector).on("click", ".add-collaborateur", function() {
      $("#fancy_add i").show();
      var classList = $(this).parent().parent().attr('class').split(/\s+/);
      var add_to_node;
      $.each(classList, function(index, item) {
        if (item != "temp" && item != "node" && item != "child" && item != "ui-draggable" && item != "ui-droppable" && !regx.test(item)) {
          add_to_node = item;
          return;
        }
      });
      $("#add_node_collaborateur").off("click").on("click", function(e) {
        e.preventDefault();

        var tipo_nodo = "";
        var new_firstname = $("#new_node_firstname");
        var new_lastname = $("#new_node_lastname");
        var new_poste = $("#new_node_poste");
        var new_act_id_user = $("#new_node_user");
        var new_create_act_id_user = $("#new_node_create_user");
        var firstname = new_firstname.val();
        var lastname = new_lastname.val();
        var $node = $("li." + add_to_node + ":not('.temp')");
        var childs = $("#org li").size() + $("#upload-chart li").size() + 1;
        tipo_nodo += "child unicc-new" + childs;
        var append_text = "<li class='" + tipo_nodo + "' id='c-new" + childs + "'>";
        append_text += "<span class='label_node nowrapt'><a href='' onclick='return false;'><span class='firstName'>" + firstname + "</span>&nbsp; <span class='lastName'>" + lastname +"</span></a></br><i class='post'>" + new_poste.find('option:selected').text() + "</i><input type='hidden' name='poste' class='poste' value='" + new_poste.val() + "'><input type='hidden' name='actIdUser' class='actIdUser' value='" + new_act_id_user.val() + "'><input type='hidden' name='createActIdUser' class='createActIdUser' value='" + new_create_act_id_user.is(':checked') + "'><div style='display:table;'><img src='../../resources/img/avatar.png' style='height: 52px;width: 45px;background-color: #99A596;'></div></span>";
        append_text += "</li>";
        if ($node.find("ul").size() == 0) {
          append_text = "<ul>" + append_text + "</ul>";
          $node.append(append_text);
        } else {
          $node.find("ul:eq(0)").append(append_text);
        }
        new_firstname.val("");
        new_lastname.val("");
        //new_poste.find('option:selected').remove();
        new_poste.val(new_poste.find('option:first').val());
        hideBox();
        init_tree();
        scroll()
      });
      $("#fancy_add_collaborateur").show();
      throw new Error('This is not an error. This is just to abort javascript');
    });

    function hideBox() {
      $("#fancy_edit_entity, #fancy_add_entity, #fancy_edit_collaborateur, #fancy_add_collaborateur").hide();
    }

    $("#fancy_edit_entity i,#fancy_add_entity i,#fancy_edit_collaborateur i,#fancy_add_collaborateur i").click(function() {
      hideBox();
    })

  }

  function remChild(removing) {

    $("#upload-chart").append(removing);
    $("#upload-chart ul li").each(function() {
      var Orgli = $(this);
	  var d = Orgli.hasClass("direction");
	  Orgli.removeAttr("class").addClass("node").addClass("child").clone();
	  if(d) Orgli.addClass("direction");
      $(this).remove();
      $("#upload-chart").append(Orgli);
    });
    $("#upload-chart ul").remove();
    var sideLi = $("#upload-chart").html();
    $("#upload-chart").empty();
    $("#upload-chart").append(sideLi);
  }



})(jQuery);
