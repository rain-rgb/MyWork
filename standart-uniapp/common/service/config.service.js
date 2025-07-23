let BASE_URL = ''


if (process.env.NODE_ENV == 'development') {
	// BASE_URL = 'http://localhost:8089/jeecg-boot/' // 开发环境
	 // 集团标准版环境 http://47.96.161.157/jeecg-boot/ 集团标准版环境
	BASE_URL = 'http://47.98.32.195:8090/jeecg-boot' // 开发环境
	// BASE_URL = 'http://47.98.32.195:8081/jeecg-boot' // 开发环境
} else {
	BASE_URL = 'http://z.traiot.cn/jeecg-boot' // 生产环境
}
let staticDomainURL = BASE_URL + '/sys/common/static';

const configService = {
	apiUrl: BASE_URL,
	staticDomainURL: staticDomainURL
};

export default configService
