var appealVM = new Vue({
	el : '#appeal',
	data : {
		orderNo : '',
		merchantName : '',
		gatheringChannelCode : '',
		gatheringChannelDictItems : [],
		receiverUserName : '',
		appealType : '',
		appealTypeDictItems : [],
		appealState : '',
		appealStateDictItems : [],
		initiationStartTime : dayjs().format('YYYY-MM-DD'),
		initiationEndTime : dayjs().format('YYYY-MM-DD'),

		auditPlatformOrderFlag : false,
		auditPlatformOrder : '',
		auditNote : '',
	},
	computed : {},
	created : function() {
	},
	mounted : function() {
		this.loadGatheringChannelDictItem();
		this.loadAppealTypeDictItem();
		this.loadAppealStateDictItem();
		this.initTable();
	},
	methods : {
		/**
		 * 加载收款渠道字典项
		 */
		loadGatheringChannelDictItem : function() {
			var that = this;
			that.$http.get('/dictconfig/findDictItemInCache', {
				params : {
					dictTypeCode : 'gatheringChannel'
				}
			}).then(function(res) {
				this.gatheringChannelDictItems = res.body.data;
			});
		},

		loadAppealTypeDictItem : function() {
			var that = this;
			that.$http.get('/dictconfig/findDictItemInCache', {
				params : {
					dictTypeCode : 'appealType'
				}
			}).then(function(res) {
				this.appealTypeDictItems = res.body.data;
			});
		},

		loadAppealStateDictItem : function() {
			var that = this;
			that.$http.get('/dictconfig/findDictItemInCache', {
				params : {
					dictTypeCode : 'appealState'
				}
			}).then(function(res) {
				this.appealStateDictItems = res.body.data;
			});
		},

		initTable : function() {
			var that = this;
			$('.appeal-table').bootstrapTable({
				classes : 'table table-hover',
				height : 490,
				url : '/appeal/findAppealByPage',
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
						orderNo : that.orderNo,
						merchantName : that.merchantName,
						gatheringChannelCode : that.gatheringChannelCode,
						receiverUserName : that.receiverUserName,
						appealType : that.appealType,
						appealState : that.appealState,
						initiationStartTime : that.initiationStartTime,
						initiationEndTime : that.initiationEndTime
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
					field : 'orderNo',
					title : '订单号'
				}, {
					field : 'merchantName',
					title : '商户'
				}, {
					title : '收款渠道/收款金额',
					formatter : function(value, row, index, field) {
						var text = row.gatheringChannelName + '/' + row.gatheringAmount + '元';
						return text;
					}
				}, {
					title : '接单人/接单时间',
					formatter : function(value, row, index, field) {
						if (row.receiverUserName == null) {
							return;
						}
						var text = row.receiverUserName + '/' + row.receivedTime;
						return text;
					}
				}, {
					field : 'appealTypeName',
					title : '申诉类型'
				}, {
					field : 'stateName',
					title : '状态'
				}, {
					field : 'initiationTime',
					title : '发起时间'
				}, {
					title : '操作',
					formatter : function(value, row, index) {
						if (row.state == '1') {
							return [ '<button type="button" class="deal-btn btn btn-outline-danger btn-sm">马上处理</button>' ].join('');
						}
					},
					events : {
						'click .deal-btn' : function(event, value, row, index) {
							that.cancelOrder(row.id);
						}
					}
				} ]
			});
		},

		refreshTable : function() {
			$('.appeal-table').bootstrapTable('refreshOptions', {
				pageNumber : 1
			});
		},

		cancelOrder : function(id) {
			var that = this;
			layer.confirm('确定要取消订单吗?', {
				icon : 7,
				title : '提示'
			}, function(index) {
				layer.close(index);
				that.$http.get('/platformOrder/cancelOrder', {
					params : {
						id : id
					}
				}).then(function(res) {
					layer.alert('操作成功!', {
						icon : 1,
						time : 3000,
						shade : false
					});
					that.refreshTable();
				});
			});
		},

		showAuditOrderModal : function(platformOrder) {
			this.auditPlatformOrderFlag = true;
			this.auditPlatformOrder = platformOrder;
			this.auditNote = '';
		},

		audit : function(action) {
			var that = this;
			var url = '/platformOrder/customerServiceConfirmToPaid';
			if (action == 2) {
				url = '/platformOrder/unpaidCancelOrder';
			}
			that.$http.get(url, {
				params : {
					id : that.auditPlatformOrder.id,
					note : that.auditNote
				}
			}).then(function(res) {
				layer.alert('操作成功!', {
					icon : 1,
					time : 3000,
					shade : false
				});
				this.auditPlatformOrderFlag = false;
				that.refreshTable();
			});
		}
	}
});