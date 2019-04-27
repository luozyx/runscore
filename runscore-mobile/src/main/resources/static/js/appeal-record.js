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
		showViewDetailsFlag : false,
		selectedAppealRecord : {},
		showUploadSreenshotFlag : false,
		userSreenshotIds : ''
	},
	computed : {},
	created : function() {
	},
	mounted : function() {
		headerVM.title = '申诉记录';
		headerVM.showBackFlag = true;
		var that = this;
		that.loadGatheringChannelDictItem();
		that.loadAppealTypeDictItem();
		that.loadAppealStateDictItem();
		that.loadByPage();

		$('.upload-sreenshot').on('filebatchuploadsuccess', function(event, data) {
			that.userSreenshotIds = data.response.data.join(',');
			that.uploadSreenshotInner();
		});
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
				that.showViewDetailsPage(appealId);
			});
		},

		showAppealRecordPage : function() {
			headerVM.title = '申诉记录';
			headerVM.showBackFlag = true;
			this.query();
			this.showAppealRecordFlag = true;
			this.showViewDetailsFlag = false;
			this.showUploadSreenshotFlag = false;
		},

		showViewDetailsPage : function(appealId) {
			var that = this;
			that.$http.get('/appeal/findAppealById', {
				params : {
					appealId : appealId
				}
			}).then(function(res) {
				this.selectedAppealRecord = res.body.data;
				headerVM.title = '申诉详情';
				headerVM.showBackFlag = false;
				this.showAppealRecordFlag = false;
				this.showViewDetailsFlag = true;
				this.showUploadSreenshotFlag = false;
			});
		},

		showUploadSreenshotPage : function() {
			headerVM.title = '上传截图';
			headerVM.showBackFlag = false;
			this.showAppealRecordFlag = false;
			this.showViewDetailsFlag = false;
			this.showUploadSreenshotFlag = true;
			this.initFileUploadWidget();
		},

		initFileUploadWidget : function() {
			var initialPreview = [];
			var initialPreviewConfig = [];
			$('.upload-sreenshot').fileinput('destroy').fileinput({
				uploadAsync : false,
				browseOnZoneClick : true,
				showBrowse : false,
				showCaption : false,
				showClose : true,
				showRemove : false,
				showUpload : false,
				dropZoneTitle : '点击选择图片',
				dropZoneClickTitle : '',
				layoutTemplates : {
					footer : ''
				},
				maxFileCount : 2,
				uploadUrl : '/storage/uploadPic',
				enctype : 'multipart/form-data',
				allowedFileExtensions : [ 'jpg', 'png', 'bmp', 'jpeg' ],
				initialPreview : initialPreview,
				initialPreviewAsData : true,
				initialPreviewConfig : initialPreviewConfig
			});
		},

		uploadSreenshot : function() {
			var filesCount = $('.upload-sreenshot').fileinput('getFilesCount');
			if (filesCount == 0) {
				layer.alert('请上传截图', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			$('.upload-sreenshot').fileinput('upload');
		},

		uploadSreenshotInner : function() {
			var that = this;
			that.$http.get('/appeal/userUploadSreenshot', {
				params : {
					appealId : that.selectedAppealRecord.id,
					userSreenshotIds : that.userSreenshotIds
				}
			}).then(function(res) {
				layer.alert('操作成功!', {
					icon : 1,
					time : 3000,
					shade : false
				});
				that.showViewDetailsPage(that.selectedAppealRecord.id);
			});
		}
	}
});