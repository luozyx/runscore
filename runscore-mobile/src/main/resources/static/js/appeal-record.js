var appealRecordVM = new Vue({
	el : '#appeal-record',
	data : {
		gatheringChannelCode : '',
		gatheringChannelDictItems : [],
		orderNo : '',
		appealType : '',
		appealTypeDictItems : [],
		appealState : '',
		appealStateDictItems : [],
		initiationTime : dayjs().format('YYYY-MM-DD'),
		appealRecords : [],
		pageNum : 1,
		totalPage : 1,
		showAppealRecordFlag : true,
		selectedAppealRecord : {}
	},
	computed : {},
	created : function() {
	},
	mounted : function() {
		headerVM.title = '申诉记录';
		headerVM.showBackFlag = true;
		this.loadGatheringChannelDictItem();
		this.loadAppealTypeDictItem();
		this.loadAppealStateDictItem();
		this.loadByPage();
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

		query : function() {
			this.pageNum = 1;
			this.loadByPage();
		},

		prePage : function() {
			this.pageNum = this.pageNum - 1;
			this.loadByPage();
		},

		nextPage : function() {
			this.pageNum = this.pageNum + 1;
			this.loadByPage();
		},

		loadByPage : function() {
			var that = this;
			that.$http.get('/appeal/findUserAppealRecordByPage', {
				params : {
					pageSize : 5,
					pageNum : that.pageNum,
					gatheringChannelCode : that.gatheringChannelCode,
					orderNo : that.orderNo,
					appealType : that.appealType,
					appealState : that.appealState,
					initiationStartTime : that.initiationTime,
					initiationEndTime : that.initiationTime
				}
			}).then(function(res) {
				that.appealRecords = res.body.data.content;
				that.pageNum = res.body.data.pageNum;
				that.totalPage = res.body.data.totalPage;
			});
		},

		userCancelAppeal : function(appealId) {
			var that = this;
			that.$http.get('/appeal/userCancelAppeal', {
				params : {
					appealId : appealId
				}
			}).then(function(res) {
				layer.alert('撤销成功', {
					icon : 1,
					time : 2000,
					shade : false
				});
				that.query();
			});
		},
		
		showAppealRecordPage : function() {
			headerVM.title = '申诉记录';
			headerVM.showBackFlag = true;
			this.showAppealRecordFlag = true;
		},

		showViewDetailsPage : function(appealRecord) {
			this.selectedAppealRecord = appealRecord;
			headerVM.title = '申诉详情';
			headerVM.showBackFlag = false;
			this.showAppealRecordFlag = false;
		}
	}
});