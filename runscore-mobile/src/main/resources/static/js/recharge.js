var rechargeVM = new Vue({
	el : '#recharge',
	data : {
		selectedRechargeWay : '',
		rechargeWays : [ {
			rechargeWayCode : 'alipay',
			rechargeWayName : '支付宝',
			icon : '/images/recharge/alipay.png'
		}, {
			rechargeWayCode : 'wechat',
			rechargeWayName : '微信',
			icon : '/images/recharge/wechat.png'
		} ],
		userName : '',
		rechargeAmount : ''
	},
	computed : {},
	created : function() {
	},
	mounted : function() {
		headerVM.title = '充值';
		headerVM.showBackFlag = true;
		this.getUserAccountInfo();

	},
	methods : {
		chooseRechargeWay : function(rechargeWay) {
			this.selectedRechargeWay = rechargeWay;
		},

		/**
		 * 获取用户账号信息
		 */
		getUserAccountInfo : function() {
			var that = this;
			that.$http.get('/userAccount/getUserAccountInfo').then(function(res) {
				if (res.body.data != null) {
					that.userName = res.body.data.userName;
				}
			});
		},

		confirmRecharge : function() {
			var that = this;
			if (that.selectedRechargeWay == null || that.selectedRechargeWay == '') {
				layer.alert('请选择支付方式');
				return;
			}
			if (that.rechargeAmount == null || that.rechargeAmount == '') {
				layer.alert('请输入充值金额');
				return;
			}
			layer.open({
				title : '提示',
				icon : 7,
				closeBtn : 0,
				btn : [],
				content : '正在创建充值订单...',
				time : 2000
			});
			that.$http.post('/recharge/generateRechargeOrderWithAbcyzf', {
				rechargeWayCode : that.selectedRechargeWay.rechargeWayCode,
				rechargeAmount : that.rechargeAmount
			}, {
				emulateJSON : true
			}).then(function(res) {
				console.log(res);
				layer.open({
					title : '提示',
					icon : '1',
					closeBtn : 0,
					btn : [],
					content : '充值订单创建成功,正在跳转到支付页面!',
					time : 2000,
					end : function() {
						window.location.href = res.body.data.payUrl;
					}
				});
			});
		}
	}
});