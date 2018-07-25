$(document).ready(function() {
	setTimeout(PrimeFaces.widget.DataTable.prototype.makeRowsDraggable, 1000);
});

PrimeFaces.widget.DataTable.prototype.makeRowsDraggable = function() {
	var $this = this;
	if(typeof(this.tbody) !== 'undefined') {
		this.tbody.sortable({
			placeholder: 'ui-datatable-rowordering ui-state-active',
			cursor: 'move',
			handle: 'td.dtHandle',
			appendTo: document.body,
			start: function(event, ui) {
				ui.helper.css('z-index', ++PrimeFaces.zindex);
			},
			helper: function(event, ui) {
				var cells = ui.children(),
					helper = $('<div class="ui-datatable ui-widget"><table><tbody></tbody></table></div>'),
					helperRow = ui.clone(),
					helperCells = helperRow.children();

				for(var i = 0; i < helperCells.length; i++) {
					helperCells.eq(i).width(cells.eq(i).width());
				}

				helperRow.appendTo(helper.find('tbody'));

				return helper;
			},
			update: function(event, ui) {
				var fromIndex = ui.item.data('ri'),
					toIndex = $this.paginator ? $this.paginator.getFirst() + ui.item.index(): ui.item.index();

				$this.syncRowParity();

				var options = {
					source: $this.id,
					process: $this.id,
					params: [
						{name: $this.id + '_rowreorder', value: true},
						{name: $this.id + '_fromIndex', value: fromIndex},
						{name: $this.id + '_toIndex', value: toIndex},
						{name: this.id + '_skipChildren', value: true}
					]
				};

				if($this.hasBehavior('rowReorder')) {
					$this.cfg.behaviors['rowReorder'].call($this, options);
				}
				else {
					PrimeFaces.ajax.Request.handle(options);
				}
			},
			change: function(event, ui) {
				if($this.cfg.scrollable) {
					PrimeFaces.scrollInView($this.scrollBody, ui.placeholder);
				}
			}
		});
	}
};
