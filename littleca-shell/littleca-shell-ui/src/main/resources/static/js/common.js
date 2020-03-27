var webCache = new WebStorageCache();


const myAxios = axios.create({
    baseURL: '/api',
    transformRequest: [function (data) {
        console.info((typeof data));
        return qs.stringify(data);
    }]
})
myAxios.interceptors.request.use(
    function (config) {
        config.headers['token'] = getToken()
        return config;
    }
);

myAxios.interceptors.response.use(
    function (config) {
        return config;
    },
    function (error) {
        switch (error.response.status) {
            default:
                app.$message({
                    showClose: true,
                    message: error.msg
                });
                break;
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
    app.$message({
        showClose: true,
        message: '会话过期,重新登录',
        type: 'warning'
    });
    setTimeout(function () {
        window.location.href = "/login.html";

    }, 3000)

}
