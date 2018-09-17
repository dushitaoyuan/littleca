/*var token=getParamFromUrl("token");
if(token){//token存活30分钟
	var now=new Date();
	var expire=new Date(now.getTime()+30*60*1000);
	//.ifcert.org.cn
	Cookies.set('token', token, { expires:expire ,path:'/',domain: 'localhost' });
}
*/
function login(){

	var username=$("form[name='loginForm'] input[name='username']").val();
	var password=$("form[name='loginForm'] input[name='password']").val();
	if(!username){
		layer.alert("用户名不可为空");
		return;
	}
	if(!password){
		layer.alert("密码不可为空");
		return;
	}
	
	$.ajax({  
        url : 'api/login',  
        async : true, 
        type : "post", 
        contentType : "application/json",
        data : JSON.stringify({"username":username,"password":password}),
        dataType : "json",  
        success : function(result) {
        	if(result.status!=1){
        		layer.alert(result.msg);
        		return;
        	}else{
        		var now=new Date();
            	var expire=new Date(now.getTime()+30*60*1000);
            	localStorage.setItem("username",result.info.username);
            	Cookies.set('token', result.info.token, { expires:expire ,path:'/',domain:document.domain});
            	layer.alert("登录成功");
            	window.location.href="../create.html";
        	}
        	
        }
        });
	
}