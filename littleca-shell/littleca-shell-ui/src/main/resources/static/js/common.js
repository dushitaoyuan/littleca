var webCache = new WebStorageCache();

const myAxios = axios.create({
    baseURL: '/api',
    transformRequest: [function (data, headers) {
        let requestType = headers['Content-Type'];
        if (requestType) {
            requestType = requestType.toLowerCase();
            //json处理
            if (requestType.indexOf('json') > 0) {
                return JSON.stringify(data);
            }
            //form处理
            if (requestType.indexOf('x-www-form-urlencoded') > 0) {
                return Qs.stringify(data, {arrayFormat: 'brackets'});
            }
        }
        //其他
        return data;
    }]
})
/**
 * 请求鉴权
 */
myAxios.interceptors.request.use(
    function (config) {
        if (config.url.indexOf("login") == -1) {
            let token = getToken();
            if(token){
                config.headers['token'] = getToken()
            }
        }
        return config;
    }
);
/**
 * 错误处理
 */
myAxios.interceptors.response.use(
    function (config) {
        if (!isSuccess(config.data)) {
            commonError(config.data.msg)
        }
        return config;
    },
    function (error) {
        console.info("error", error)
        if (error && !isSuccess(error.response.data)) {
            var errorData = response.data
            console.error("error code \t" + errorData.code + "\t error msg" + errorData.msg);
            switch (data.status == 0) {
                default:
                    commonError(errorData.msg);
                    break;
            }
        }

    }
);

function getToken() {
    var token = webCache.get('token');
    if (token) {
        return token;
    }
    toLogin();
}

function toLogin() {
    commonError('会话过期,重新登录')
    setTimeout(function () {
        window.location.href = "/login.html";
    }, 3000)
}

function commonError(msg) {
    if (msg) {
        app.$message.error(msg)
    }
}

function isSuccess(data) {
    if (data && data.status == 1) {
        return true;
    }
    return false;
}
