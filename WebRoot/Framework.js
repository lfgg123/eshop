var app = angular.module('myApp', [ 'ui.router', 'ng.ueditor' ]);
app.controller('myCtrl', function($scope, $http, $window) {

});
app.config([ '$stateProvider', '$urlRouterProvider',
		function($stateProvider, $urlRouterProvider) {
			$urlRouterProvider.when("", "/view");
			$stateProvider.state('view', {
				url : '/view',
				views : {
					'front' : {
						templateUrl : 'publicPage/frontPage.jsp'
					},
					'footer' : {
						templateUrl : 'publicPage/footerPage.jsp'
					}
				}
			})
			//--系统基础设置
			.state('sysset', {
				url : '/sysset',
				views : {
					'front' : {
						templateUrl : 'sysset/sysment.jsp'
					},
					'footer' : {
						templateUrl : 'publicPage/footerPage.jsp'
					}
				}
			}) 
			//--平台账号设置
			.state('account', {
				url : '/account',
				views : {
					'front' : {
						templateUrl : 'sysset/account.jsp'
					},
					'footer' : {
						templateUrl : 'publicPage/footerPage.jsp'
					}
				}
			})
			//-- 平台模块
			.state('module', {
				url : '/module',
				views : {
					'front' : {
						templateUrl : 'sysset/module.jsp'
					},
					'footer' : {
						templateUrl : 'publicPage/footerPage.jsp'
					}
				}
			})
			//--角色
			.state('role', {
				url : '/role',
				views : {
					'front' : {
						templateUrl : 'sysset/role.jsp'
					},
					'footer' : {
						templateUrl : 'publicPage/footerPage.jsp'
					}
				}
			})
			//--门店
			.state('stores', {
				url : '/stores',
				views : {
					'front' : {
						templateUrl : 'sysset/stores.jsp'
					},
					'footer' : {
						templateUrl : 'publicPage/footerPage.jsp'
					}
				}
			})
			//--消息
			.state('dope', {
				url : '/dope',
				views : {
					'front' : {
						templateUrl : 'sysset/dope.jsp'
					},
					'footer' : {
						templateUrl : 'publicPage/footerPage.jsp'
					}
				}
			})
			//--内容管理
			.state('substance', {
				url : '/substance',
				views : {
					'front' : {
						templateUrl : 'sysset/substance.jsp'
					},
					'footer' : {
						templateUrl : 'publicPage/footerPage.jsp'
					}
				}
			})
			//--会员
			.state('member', {
				url : '/member',
				views : {
					'front' : {
						templateUrl : 'member/member.jsp'
					},
					'footer' : {
						templateUrl : 'publicPage/footerPage.jsp'
					}
				}
			})
			//--酒币
			.state('wine', {
				url : '/wine',
				views : {
					'front' : {
						templateUrl : 'member/wine.jsp'
					},
					'footer' : {
						templateUrl : 'publicPage/footerPage.jsp'
					}
				}
			})
			//--社区
			.state('bbs', {
				url : '/bbs',
				views : {
					'front' : {
						templateUrl : 'member/bbs.jsp'
					},
					'footer' : {
						templateUrl : 'publicPage/footerPage.jsp'
					}
				}
			})
			//--私人定制
			.state('myself', {
				url : '/myself',
				views : {
					'front' : {
						templateUrl : 'myself/myself.jsp'
					},
					'footer' : {
						templateUrl : 'publicPage/footerPage.jsp'
					}
				}
			})
			//--商品信息
			.state('proinfo', {
				url : '/proinfo',
				views : {
					'front' : {
						templateUrl : 'product/proinfo.jsp'
					},
					'footer' : {
						templateUrl : 'publicPage/footerPage.jsp'
					}
				}
			})
			//--分类西悉尼
			.state('classinfo', {
				url : '/classinfo',
				views : {
					'front' : {
						templateUrl : 'product/classinfo.jsp'
					},
					'footer' : {
						templateUrl : 'publicPage/footerPage.jsp'
					}
				}
			})
			//--规格信息
			.state('specinfo', {
				url : '/specinfo',
				views : {
					'front' : {
						templateUrl : 'product/specinfo.jsp'
					},
					'footer' : {
						templateUrl : 'publicPage/footerPage.jsp'
					}
				}
			})
			//--限时抢购
			.state('timebuy', {
				url : '/timebuy',
				views : {
					'front' : {
						templateUrl : 'product/timebuy.jsp'
					},
					'footer' : {
						templateUrl : 'publicPage/footerPage.jsp'
					}
				}
			})
			//--精选
			.state('choice', {
				url : '/choice',
				views : {
					'front' : {
						templateUrl : 'product/choice.jsp'
					},
					'footer' : {
						templateUrl : 'publicPage/footerPage.jsp'
					}
				}
			})
			//--跑腿
			.state('runinfo', {
				url : '/runinfo',
				views : {
					'front' : {
						templateUrl : 'product/runinfo.jsp'
					},
					'footer' : {
						templateUrl : 'publicPage/footerPage.jsp'
					}
				}
			})
			//--满减活动
			.state('enough_cut', {
				url : '/enough_cut',
				views : {
					'front' : {
						templateUrl : 'product/enough_cut.jsp'
					},
					'footer' : {
						templateUrl : 'publicPage/footerPage.jsp'
					}
				}
			})
		}
	]);
