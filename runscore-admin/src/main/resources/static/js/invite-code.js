var inviteCodeVM = new Vue({
	el : '#invite-code',
	data : {
		inviter : ''
	},
	computed : {},
	created : function() {
	},
	mounted : function() {
		var that = this;
		that.initTable();
	},
	methods : {

		loadGatheringCodeStateDictItem : function() {
			var that = this;
			that.$http.get('/dictconfig/findDictItemInCache', {
				params : {
					dictTypeCode : 'gatheringCodeState'
				}
			}).then(function(res) {
				this.gatheringCodeStateDictItems = res.body.data;
			});
		},

		initTable : function() {
			var that = this;
			$('.invite-code-table').bootstrapTable({
				classes : 'table table-hover',
				height : 490,
				url : '/inviteCode/findGatheringCodeByPage',
				pagination : true,
				sidePagination : 'server',
				pageNumber : 1,
				pageSize : 10,
				pageList : [ 10, 25, 50, 100 ],
				queryParamsType : '',
				queryParams : function(params) {
					var condParam = {
						pageSize : params.pageSize,
						pageNum : params.pageNumber,
						inviter : that.inviter
					};
					return condParam;
				},
				responseHandler : function(res) {
					return {
						total : res.data.total,
						rows : res.data.content
					};
				},
				columns : [ {
					field : 'inviterUserName',
					title : '邀请人'
				}, {
					field : 'code',
					title : '邀请码'
				}, {
					field : 'inviteeUserName',
					title : '受邀人'
				}, {
					title : '邀请码状态',
					formatter : function(value, row, index, field) {
						if (row.inviteeUserName != null && row.inviteeUserName != '') {
							return '已使用';
						}
					}
				}, {
					field : 'createTime',
					title : '创建时间'
				}, {
					field : 'periodOfValidity',
					title : '有效期'
				}, {
					title : '操作',
					formatter : function(value, row, index) {
						return [ '<button type="button" class="view-gathering-code-btn btn btn-outline-primary btn-sm" style="margin-right: 4px;">延长有效期</button>', '<button type="button" class="del-gathering-code-btn btn btn-outline-danger btn-sm">删除</button>' ].join('');
					},
					events : {
						'click .view-gathering-code-btn' : function(event, value, row, index) {
						},
						'click .del-gathering-code-btn' : function(event, value, row, index) {
							that.delGatheringCode(row.id);
						}
					}
				} ]
			});
		},

		refreshTable : function() {
			$('.invite-code-table').bootstrapTable('refreshOptions', {
				pageNumber : 1
			});
		},

		delGatheringCode : function(gatheringCodeId) {
			var that = this;
			that.$http.get('/gatheringCode/delGatheringCodeById', {
				params : {
					id : gatheringCodeId,
				}
			}).then(function(res) {
				layer.alert('操作成功!', {
					icon : 1,
					time : 3000,
					shade : false
				});
				that.addOrUpdateGatheringCodeFlag = false;
				that.refreshTable();
			});
		}
	}
});