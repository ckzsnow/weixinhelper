var weixinGroupName = '@@25e73294264aeeaf0dc01ed394f8173251479f5bb83857ab0345f7f64e5847c0';
var userName = '@cd33ebc8939d435cbd90ee43140d8991e4fa7f3a7beb0bd56a9897d1aab2d693';
function searchWeixinUserAndClick() {
	var hasFind = false;
	$('#J_ContactPickerScrollBody div[ng-repeat]').each(function(){
		var src = $($(this).find('img')[0]).attr('src');
		if(!hasFind && src != null && src != "") {
			var beginPos = src.indexOf('&username=');
			var endPos = src.indexOf('&skey=');
			var currentUserName = src.substring(beginPos + 10, endPos);
			if(currentUserName == userName) {
				hasFind = true;
				$($(this).find('.contact_item')[0]).click();
				$($('.dialog_ft.ng-scope a')[0]).click();
			}
		}
	});
	if(!hasFind) {
		$('#J_ContactPickerScrollBody')[0].scrollTop = $('#J_ContactPickerScrollBody')[0].scrollTop + 5*54;
		setTimeout(function(){
			if($('#J_ContactPickerScrollBody')[0].scrollTop <= $('#J_ContactPickerScrollBody')[0].scrollHeight + 5*54) {
				searchWeixinUserAndClick();
			} else {
				$('.ngdialog-close').click();
				window.cefQuery({
					request: 'BBZ_WEIXIN_FINISH:1',
					onSuccess: function(response) {},
					onFailure: function(error_code, error_message) {}
				});
			}
		}, 1000);
	} else {
		window.cefQuery({
			request: 'BBZ_WEIXIN_FINISH:1',
			onSuccess: function(response) {},
			onFailure: function(error_code, error_message) {}
		});
	}
}
function searchWeixinGroupAndClick() {
	var hasFind = false;
	$('.scroll-wrapper.chat_list.scrollbar-dynamic div[ng-repeat]').each(function(){
		if(!hasFind && $($(this).children()[0]).attr('data-cm').indexOf(weixinGroupName) != -1) {
			hasFind = true;
			$($(this).children()[0]).click();
			$($('.title.poi a')[0]).click();
			$('#mmpop_chatroom_members i').click();
			setTimeout(function(){searchWeixinUserAndClick();},2000);
		}
	});
	if(!hasFind) {
		setTimeout(function(){
			$('#J_NavChatScrollBody')[0].scrollTop=$('#J_NavChatScrollBody')[0].scrollTop+8*64;
			if($('#J_NavChatScrollBody')[0].scrollTop <= $('#J_NavChatScrollBody')[0].scrollHeight + 8 * 64) {
				searchWeixinGroupAndClick();
			} else {
				window.cefQuery({
					request: 'BBZ_WEIXIN_FINISH:1',
					onSuccess: function(response) {},
					onFailure: function(error_code, error_message) {}
				});
			}
		}, 1000)
	}
}
$('#J_NavChatScrollBody')[0].scrollTop=0;
setTimeout(function(){searchWeixinGroupAndClick()},1000);