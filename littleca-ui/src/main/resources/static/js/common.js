/**
 * 从url中获取参数
 * @param key
 * @returns
 */
function getParamFromUrl(key){
	var url=window.location.href;
	var index=url.indexOf("?");
	if(index<0){
		return;
	}
	var key_value=url.substring(index+1).split("&");
	for(var i=0,len=key_value.length;i<len;i++){
		var s=key_value[i].split("=");
		if(s[0]==key){
			var i=s[1].indexOf("#");
			if(i>0){
				return s[1].substring(0,i);
			}
			return s[1];
			
		}
	}
	
}


