<template>
	<view id="home">
		<cu-custom bgColor="bg-white" :isBack="true">
			<block slot="content">桩基超声波检测</block>
			<view slot="right" @click="screen">筛选</view>
		</cu-custom>
		<view class="section" v-for="(item,index) in shitilist" :key="index">
			<view class="section-text" @click="clickzj(item.id,item.testid)">
				<view class="">
					设备名称:<span>{{item.shebeino_dictText !==""&&item.shebeino_dictText !==null?item.shebeino_dictText:'暂无数据'}}</span>
				</view>
				<view class="">试桩类型:<span :style="item.shizhuangleixing ==20?'color:green;font-family: cursive;font-weight:bold':item.shizhuangleixing ==21?'color:orange;font-family: cursive;font-weight:bold':item.shizhuangleixing ==22?'color:blue;font-family: cursive;font-weight:bold':''">{{item.shizhuangleixing ==20?'原桩':item.shizhuangleixing ==21?'方桩':item.shizhuangleixing ==22?'地下连续墙':'暂无数据'}}</span>
				</view>
				<view class="">测试日期:<span>{{item.ceshitime!==null&&item.ceshitime!==""?item.ceshitime:'暂无数据'}}</span>
				</view>
				<view class="">上传时间:<span>{{item.dangqiantime !==null&&item.dangqiantime !==""?item.dangqiantime:'暂无数据'}}</span>
				</view>
				<view class="">
					试桩编号:<span>{{item.shizhuangno !==null&&item.shizhuangno !==""?item.shizhuangno:'暂无数据'}}</span>
				</view>
				<view class="">
					桩径:<span>{{item.zhuangjing !==''&& item.zhuangjing!==null?item.zhuangjing:'未检测'}}</span>
				</view>
				<view class="">桩长:<span>{{item.zhuangchang !==''&& item.zhuangchang!==null?item.zhuangchang:'暂无数据'}}</span></view>
				<view class="">管数:<span>{{item.guanshu !==''&&item.guanshu!==null?item.guanshu:'暂无数据'}}</span>
				</view>
				<view class="">剖面数:<span>{{item.poumianshu !==''&&item.poumianshu!==null?item.poumianshu:'暂无数据'}}</span>
				</view>
				<view class="">
						经度:<span>{{item.jingdu !==''&&item.jingdu!==null?item.jingdu:'暂无数据'}}</span>
				</view>
				<view class="">
					纬度:<span>{{item.weidu !==null&&item.weidu!==""?item.weidu:'暂无数据'}}</span>
				</view>
				<view class="">
					采样频率:<span>{{item.caiyangpinlv !==''&&item.caiyangpinlv!==null?item.caiyangpinlv:'暂无数据'}}</span>
				</view>
				<view class="">采样长度:<span>{{item.caiyanglength !==''&&item.caiyanglength!==null?item.caiyanglength:'暂无数据'}}</span>
				</view>
				<view class="">声测管内径:<span>{{item.shengceguanneijing !==''&&item.shengceguanneijing !==null?item.shengceguanneijing:'暂无数据'}}</span>
				</view>
				<view class="">声测管外径:<span>{{item.shengceguanwaijing !==null&&item.shengceguanwaijing!==""?item.shengceguanwaijing:'暂无数据'}}</span>
				</view>
				<view class="">
					方位角:<span>{{item.fangweijiao !== ''&&item.fangweijiao!==null?item.fangweijiao:'暂无数据'}}</span>
				</view>
				<view class="">轮径:<span>{{item.lunjing !==''&&item.lunjing !==null?item.lunjing:'暂无数据'}}</span>
				</view>
				<view class="">线径:<span
						>{{item.xianjing !==""&&item.xianjing !==null?item.xianjing:'暂无数据'}}</span>
				</view>
			</view>

		</view>
		<!-- 混凝土回弹弹框 -->
		<view  class="tables">
			<u-modal :show="showzj" closeOnClickOverlay @close="() => showzj = false" :showConfirmButton="false"
				showCancelButton @cancel="cancelzj" :title="title" cancelColor="#000">
				<view class="sectionwrap">
					<view class="section-all">
						<view class="section-text">
							<view class="title">
								<!-- <view class="round"></view> -->
								<view class="title-font">基础信息</view>
							</view>
							<view class="">采样频率:<span>{{zjlistdata.caiyangpinlv}}</span></view>
							<view class="">采样长度:<span>{{zjlistdata.caiyanglength}}</span></view>
							<view class="">声测管内径:<span>{{zjlistdata.shengceguanneijing}}</span></view>
							<view class="">声测管外径:<span>{{zjlistdata.shengceguanwaijing}}</span></view>
						</view>
					</view>
					<view class="" style="width: 300px;">
						<scroll-view scroll-x class="nav bg-white">
							<view class="flex text-center">
								<view class="cu-item flex-sub" :class="0==TabCur1?'text-blue cur':''"
									@tap="tabSelect1" data-id="0">
									桩信息
								</view>
								<view class="cu-item flex-sub" :class="1==TabCur1?'text-blue cur':''"
									@tap="tabSelect1" data-id="1">
									监测信息
								</view>
								<view class="cu-item flex-sub" :class="2==TabCur1?'text-blue cur':''"
									@tap="tabSelect1" data-id="2">
									曲线
								</view>
								<view class="cu-item flex-sub" :class="3==TabCur1?'text-blue cur':''"
									@tap="tabSelect1" data-id="3">
									波列图
								</view>
							</view>
						</scroll-view>
						<!-- 桩信息 -->
						<view v-if="TabCur1==0" class="tablelist section-all">
							<!-- <view class="title">
							<!<view class="round"></view> -->
							<!-- 	<view class="title-fonts">通道参数</view>
						</view> -->
							<uni-table stripe emptyText="暂无更多数据">
								<!-- <view class="section-text"> -->
								<!-- <view class=""> -->
								<!-- </view> -->
								<!-- </view> -->
								<!-- 表头行 -->
								<uni-tr>
									<uni-th align="center">桩号</uni-th>
									<uni-th align="center">桩长(m)</uni-th>
									<uni-th align="center">桩径(mm)</uni-th>
									<uni-th align="center">管数</uni-th>
									<uni-th align="center">方位角</uni-th>
									<uni-th align="center">声测管外径(mm)</uni-th>
									<uni-th align="center">声测管内径(mm)</uni-th>
									<uni-th align="center">声测管材料速度(km/s)</uni-th>
									<uni-th align="center">剖面数(mm)</uni-th>
									<uni-th align="center">采样频率(μs)</uni-th>
									<uni-th align="center">采样长度</uni-th>
									<uni-th align="center">轮径(mm)</uni-th>
									<uni-th align="center">线径(mm)</uni-th>
		
								</uni-tr>
								<!-- 表格数据行 -->
								<uni-tr>
									<uni-td align="center">{{zjlistdata.shizhuangno}}</uni-td>
									<uni-td align="center">{{zjlistdata.zhuangchang}}</uni-td>
									<uni-td align="center">{{zjlistdata.zhuangjing}}</uni-td>
									<uni-td align="center">{{zjlistdata.guanshu}}</uni-td>
									<uni-td align="center">{{zjlistdata.fangweijiao}}</uni-td>
									<uni-td align="center">{{zjlistdata.shengceguanwaijing}}</uni-td>
									<uni-td align="center">{{zjlistdata.shengceguanneijing}}</uni-td>
									<uni-td align="center">{{zjlistdata.shengceguan}}</uni-td>
									<uni-td align="center">{{zjlistdata.poumianshu}}</uni-td>
									<uni-td align="center">{{zjlistdata.caiyangpinlv}}</uni-td>
									<uni-td align="center">{{zjlistdata.caiyanglength}}</uni-td>
									<uni-td align="center">{{zjlistdata.lunjing}}</uni-td>
									<uni-td align="center">{{zjlistdata.xianjing}}</uni-td>
								</uni-tr>
							</uni-table>
							<!-- <view style="padding: 5px 13px;">
								<uni-pagination :total="totalck" title="标题文字" :current="currentck"
									@change="changeck" />
							</view> -->
						</view>
		        <!-- 监测信息 -->
						<view v-if="TabCur1==1" class="tablelist section-all">
							<uni-table stripe emptyText="暂无更多数据">
								<uni-tr>
									<view class="">
										<uni-th width="50" align="center">剖面</uni-th>
										<uni-th align="center">测试深度(m)</uni-th>
										<uni-th align="center">传感器高差(m)</uni-th>
										<uni-th align="center">跨距(mm)</uni-th>
										<uni-th align="center">增益</uni-th>
										<uni-th align="center">校零(μs)</uni-th>
										<uni-th align="center">延迟时间(μs)</uni-th>
										<uni-th align="center">高通(kHz)</uni-th>
										<uni-th align="center">低通(kHz)</uni-th>
									</view>
								</uni-tr>
								<!-- 表格数据行 -->
								<uni-tr>
									<view class="" v-show="list1.length>0">
										<uni-td width="80" align="center" v-for="(item,index) in list1"
											:key="index">
											{{item}}
										</uni-td>
									</view>
									<view class="" v-show="list1.length>0">
										<uni-td width="80" align="center" v-for="(item,index) in list2"
											:key="index">{{item}}
										</uni-td>
									</view>
									<view class="" v-show="list1.length>0">
										<uni-td width="80" align="center" v-for="(item,index) in list3"
											:key="index">{{item}}
										</uni-td>
									</view> 
									<view class="" v-show="list1.length>0">
										<uni-td width="80" align="center" v-for="(item,index) in list4"
											:key="index">{{item}}
										</uni-td>
									</view>
									<view class="" v-show="list1.length>0">
										<uni-td width="80" align="center" v-for="(item,index) in list5"
											:key="index">{{item}}
										</uni-td>
									</view>
									<view class="" v-show="list1.length>0">
										<uni-td width="80" align="center" v-for="(item,index) in list6"
											:key="index">{{item}}
										</uni-td>
									</view>
									<view class="" v-show="list1.length>0">
										<uni-td width="80" align="center" v-for="(item,index) in list7"
											:key="index">{{item}}
										</uni-td>
									</view>
									<view class="" v-show="list1.length>0">
										<uni-td width="80" align="center" v-for="(item,index) in list8"
											:key="index">{{item}}
										</uni-td>
									</view>
									<view class="" v-show="list1.length>0">
										<uni-td width="80" align="center" v-for="(item,index) in list9"
											:key="index">{{item}}
										</uni-td>
									</view>
									<view class="" v-show="list1.length>0">
										<uni-td width="80" align="center" v-for="(item,index) in list10"
											:key="index">{{item}}
										</uni-td>
									</view>
								</uni-tr>
							</uni-table>
							<!-- 	<view style="padding: 5px 13px;">
								<uni-pagination :total="totaldata" title="标题文字" :current="currentdata"
									@change="changeckdata" />
							</view> -->
						</view>
		                     <!-- 波幅 波速 -->
		
						<view v-if="TabCur1==2" class="tablelist section-all">
		
							<view class="charts-box section-all" style="width:300px;"
								v-if="chartData1.categories.length>0">
								<span class="xhlist">剖面{{pmhs1[0]}}</span>
								<qiun-data-charts type="mix" :chartData="chartData1" :ontouch="true" :opts="{yAxis:{data:[{position: 'left',title: '波幅'},
									{position: 'right',title: '波速',textAlign: 'left'}]},
									enableScroll:true,
									xAxis:{itemCount:5,scrollShow:false,disableScroll:true}}" />
							</view>
							<view class="charts-box section-all" style="width:300px;"
								v-if="chartData2.categories.length>0">
								<span class="xhlist">剖面{{pmhs2[0]}}</span>
								<qiun-data-charts type="mix" :chartData="chartData2"
									:ontouch="true" :opts="{yAxis:{data:[{position: 'left',title: '波幅'},
										{position: 'right',title: '波速',textAlign: 'left'}]},
										enableScroll:true,
										xAxis:{itemCount:5,scrollShow:false,disableScroll:true}}" />
							</view>
							<view class="charts-box section-all" style="width:300px;"
								v-if="chartData3.categories.length>0">
								<span class="xhlist">剖面{{pmhs3[0]}}</span>
								<qiun-data-charts type="mix" :chartData="chartData3"
									:ontouch="true" :opts="{yAxis:{data:[{position: 'left',title: '波幅'},
										{position: 'right',title: '波速',textAlign: 'left'}]},
										enableScroll:true,
										xAxis:{itemCount:5,scrollShow:false,disableScroll:true}}" />
							</view>
							<view class="charts-box section-all" style="width:300px;"
								v-if="chartData4.categories.length>0">
								<span class="xhlist">剖面{{pmhs4[0]}}</span>
								<qiun-data-charts type="mix" :chartData="chartData4"
									:ontouch="true" :opts="{yAxis:{data:[{position: 'left',title: '波幅'},
										{position: 'right',title: '波速',textAlign: 'left'}]},
										enableScroll:true,
										xAxis:{itemCount:5,scrollShow:false,disableScroll:true}}" />
							</view>
		
							<view class="charts-box section-all" style="width:300px;"
								v-if="chartData5.categories.length>0">
								<span class="xhlist">剖面{{pmhs5[0]}}</span>
								<qiun-data-charts type="mix" :chartData="chartData5"
									:ontouch="true" :opts="{yAxis:{data:[{position: 'left',title: '波幅'},
										{position: 'right',title: '波速',textAlign: 'left'}]},
										enableScroll:true,
										xAxis:{itemCount:5,scrollShow:false,disableScroll:true}}" />
							</view>
							<view class="charts-box section-all" style="width:300px;"
								v-if="chartData6.categories.length>0">
								<span class="xhlist">剖面{{pmhs6[0]}}</span>
								<qiun-data-charts type="mix" :chartData="chartData6"
									:ontouch="true" :opts="{yAxis:{data:[{position: 'left',title: '波幅'},
										{position: 'right',title: '波速',textAlign: 'left'}]},
										enableScroll:true,
										xAxis:{itemCount:5,scrollShow:false,disableScroll:true}}" />
							</view>
							<view class="charts-box section-all" style="width:300px;"
								v-if="chartData7.categories.length>0">
								<span class="xhlist">剖面{{pmhs7[0]}}</span>
								<qiun-data-charts type="mix" :chartData="chartData7"
									:ontouch="true" :opts="{yAxis:{data:[{position: 'left',title: '波幅'},
										{position: 'right',title: '波速',textAlign: 'left'}]},
										enableScroll:true,
										xAxis:{itemCount:5,scrollShow:false,disableScroll:true}}" />
							</view>
		
							<view class="charts-box section-all" style="width:300px;"
								v-if="chartData8.categories.length>0">
								<span class="xhlist">剖面{{pmhs8[0]}}</span>
								<qiun-data-charts type="mix" :chartData="chartData8"
									:ontouch="true" :opts="{yAxis:{data:[{position: 'left',title: '波幅'},
										{position: 'right',title: '波速',textAlign: 'left'}]},
										enableScroll:true,
										xAxis:{itemCount:5,scrollShow:false,disableScroll:true}}" />
							</view>
							<view class="charts-box section-all" style="width:300px;"
								v-if="chartData9.categories.length>0">
								<span class="xhlist">剖面{{pmhs9[0]}}</span>
								<qiun-data-charts type="mix" :chartData="chartData9"
									:ontouch="true" :opts="{yAxis:{data:[{position: 'left',title: '波幅'},
										{position: 'right',title: '波速',textAlign: 'left'}]},
										enableScroll:true,
										xAxis:{itemCount:5,scrollShow:false,disableScroll:true}}" />
							</view>
							<view class="charts-box section-all" style="width:300px;"
								v-if="chartData10.categories.length>0">
								<span class="xhlist">剖面{{pmhs10[0]}}</span>
								<qiun-data-charts type="mix" :chartData="chartData10"
									:ontouch="true" :opts="{yAxis:{data:[{position: 'left',title: '波幅'},
										{position: 'right',title: '波速',textAlign: 'left'}]},
										enableScroll:true,
										xAxis:{itemCount:5,scrollShow:false,disableScroll:true}}" />
							</view>
		
							<!-- <view style="padding: 5px 13px;">
								<uni-pagination :total="totalfx" title="标题文字" :current="currentfx"
									@change="changefx" />
							</view> -->
						</view>
		                        <!-- 波列图 -->
						<view v-if="TabCur1==3" class="tablelist section-all">
		
							<view class="">
								<!-- <image  :src="" mode=""></image> -->
								<u--image :showLoading="true" v-for="(item,index) in detailList" :key="index"
									:src="urls+'/chaoshengbo/chaoshengboSybltsj/list2?id='+item.id"></u--image>
							</view>
		
		
						</view>
		
		
					</view>
				</view>
		
			</u-modal>
		</view>
		<!-- 查询设备 -->
			<view class="screen" v-if="show" @confirm="confirm">
			<view class="screen-modal">
				<view class="screen-modal-item">
					<view class="screen-modal-item-name">设备名称：</view>
					<picker @change="PickerChange" :range="deviceNames">
						<u--input placeholder="请选择设备名称" border="surround" v-model="deviceName" disabled
							suffixIcon="arrow-down"></u--input>
					</picker>
	            </view>
				 <view class="screen-modal-btn">
					<u-button text="取消" @click="confirm"></u-button>
					<u-button type="primary" text="确认" @click="Choose"></u-button>
				</view> 

			</view>
		</view>
	</view>
</template>
<script>
	import configService from '../../../common/service/config.service.js'
	import timeSelector from '@/components/wing-time-selector/wing-time-selector.vue';
	import api from '@/api/apis.js'
	export default {
		name: 'departlist',
		components: {
			timeSelector,
			
		},
		data() {
			return {
				shitilist: [],
				// listdatavalue: '',
				deviceNames: [],
				devictype: [],
				deviceName: '',
				choosekey: '',
				choosevalue: '',
				sbNumber: null,
				// shows:true,
				orgcode: '',
				begintime: '',
				endtime: '',
				pageNo: 1,
				sbtype:14,
				
				TabCur: 0,
				TabCur1: 0,
				current: 1,
				pageSize: 10,
				zjlistdata: '',
				guid: '',
				gbdata: {},
				htlistdata: {},
				cktable: {},
				ckvalue: {},
				cktongdao: {},
				ckanalysis: {},
				zjdata: {},
				zjvalue: {},
				detailList: [],
				testid: '',
				htid: '',
				showzj: false,
				show: false,
				urls: '',
				// offmodal:true,
				title: '检测试验结果',
				detailslist: [],
				materilstable: [],
				gbilstable: [],
				id: '',
				list1: [],
				list2: [],
				list3: [],
				list4: [],
				list5: [],
				list6: [],
				list7: [],
				list8: [],
				list9: [],
				list10: [],
				
				
				dataSource1: [],
				dataSource2: [],
				dataSource3: [],
				dataSource4: [],
				dataSource5: [],
				dataSource6: [],
				dataSource7: [],
				dataSource8: [],
				dataSource9: [],
				dataSource10: [],
				
				pmhs1: [],
				pmhs2: [],
				pmhs3: [],
				pmhs4: [],
				pmhs5: [],
				pmhs6: [],
				pmhs7: [],
				pmhs8: [],
				pmhs9: [],
				pmhs10: [],
				chartData: {
					"categories": [

					],
					"series": [{
						"name": "厚度(mm)",
						"data": [

						]
					}]
				},
				chartData1: {
					categories: [],
					series: [{
						name: "波幅",
						index: 1,
						data: [],
						type: "line"
					}, {
						name: "波速",
						data: [],
						type: "line",
						style: "curve",
						// color: "#1890ff",
						// disableLegend: true
					}]
				},
				chartData2: {
					categories: [],
					series: [{
						name: "波幅",
						index: 1,
						data: [],
						type: "line"
					}, {
						name: "波速",
						data: [],
						type: "line",
						style: "curve",
						// color: "#1890ff",
						// disableLegend: true
					}]
				},
				chartData3: {
					categories: [],
					series: [{
						name: "波幅",
						index: 1,
						data: [],
						type: "line"
					}, {
						name: "波速",
						data: [],
						type: "line",
						style: "curve",
						// color: "#1890ff",
						// disableLegend: true
					}]
				},
				chartData4: {
					categories: [],
					series: [{
						name: "波幅",
						index: 1,
						data: [],
						type: "line"
					}, {
						name: "波速",
						data: [],
						type: "line",
						style: "curve",
						// color: "#1890ff",
						// disableLegend: true
					}]
				},
				
				chartData5: {
					categories: [],
					series: [{
						name: "波幅",
						index: 1,
						data: [],
						type: "line"
					}, {
						name: "波速",
						data: [],
						type: "line",
						style: "curve",
						// color: "#1890ff",
						// disableLegend: true
					}]
				},
				chartData6: {
					categories: [],
					series: [{
						name: "波幅",
						index: 1,
						data: [],
						type: "line"
					}, {
						name: "波速",
						data: [],
						type: "line",
						style: "curve",
						// color: "#1890ff",
						// disableLegend: true
					}]
				},
				chartData7: {
					categories: [],
					series: [{
						name: "波幅",
						index: 1,
						data: [],
						type: "line"
					}, {
						name: "波速",
						data: [],
						type: "line",
						style: "curve",
						// color: "#1890ff",
						// disableLegend: true
					}]
				},
				chartData8: {
					categories: [],
					series: [{
						name: "波幅",
						index: 1,
						data: [],
						type: "line"
					}, {
						name: "波速",
						data: [],
						type: "line",
						style: "curve",
						// color: "#1890ff",
						// disableLegend: true
					}]
				},
				chartData9: {
					categories: [],
					series: [{
						name: "波幅",
						index: 1,
						data: [],
						type: "line"
					}, {
						name: "波速",
						data: [],
						type: "line",
						style: "curve",
						// color: "#1890ff",
						// disableLegend: true
					}]
				},
				chartData10: {
					categories: [],
					series: [{
						name: "波幅",
						index: 1,
						data: [],
						type: "line"
					}, {
						name: "波速",
						data: [],
						type: "line",
						style: "curve",
						// color: "#1890ff",
						// disableLegend: true
					}]
				},
			}
		},
		onLoad() {
			this.textlist()
		},
		onReachBottom() {
			uni.showNavigationBarLoading()
			this.pageNo++
			this.textlist()
			uni.hideNavigationBarLoading()
		},
		created() {
			this.htlisttable()
			this.deviceType()
			this.urls = configService.apiUrl
		},

		methods: {
			cancelzj() {
				this.showzj = false
			},
			//点击桩基超声波检测数据弹出模态框
			clickzj(e, zjid) {
				this.showzj = true
				this.$http.get(`/chaoshengbo/chaoshengboSyjbsj/list`, {
					params: {
						id: e
					}
				}).then(res => {
					this.zjlistdata = res.data.result.records[0]
					console.log(this.zjlistdata, '基础信息加上基础信息')
					// this.guid = res.data.result.records[0].guid
			
				})
				setTimeout(() => {
					this.zjinfo()
					this.zjmonitor()
					this.Wavetrain()
				},1000)
			},
			htlisttable() {
					this.$http.get(`/chaoshengbo/chaoshengboSyjbsj/list`, {
						params: {
							liushuihao: this.id
						}
					}).then(res => {
						console.log(res, '桩基超声波检测')
						this.zjdata = res.data.result.records
					})
			},
			zjinfo() {
				// console.log(this.zjlistdata.id,'ggggg')
			
				this.$http.get(`/chaoshengbo/chaoshengboSyjbsj/list1`, {
					params: {
						id: this.zjlistdata.id
					}
				}).then(res => {
					console.log(res.data.result[0], '桩基监测信息')
			
					// this.zjvalue = res.data.result.records
					// if (res.success) {
					this.list1 = []
					this.list2 = []
					this.list3 = []
					this.list4 = []
					this.list5 = []
					this.list6 = []
					this.list7 = []
					this.list8 = []
					this.list9 = []
					this.list10 = []
					let data = res.data.result[0]
					// console.log(data,'kkkkk')
					//console.log(data,"HJhgjksqgsdjasbcfjasbcf b")
					let pm1 = data.poumian1
					let pm2 = data.poumian2
					let pm3 = data.poumian3
					let pm4 = data.poumian4
					let pm5 = data.poumian5
					let pm6 = data.poumian6
					let pm7 = data.poumian7
					let pm8 = data.poumian8
					let pm9 = data.poumian9
					let pm10 = data.poumian10
					if (pm1 != null) {
						this.list1 = pm1.split(',')
						console.log(this.list1)
					}
					if (pm2 != null) {
						this.list2 = pm2.split(',')
					}
					if (pm3 != null) {
						this.list3 = pm3.split(',')
					}
					if (pm4 != null) {
						this.list4 = pm4.split(',')
					}
					if (pm5 != null) {
						this.list5 = pm5.split(',')
					}
					if (pm6 != null) {
						this.list6 = pm6.split(',')
					}
					if (pm7 != null) {
						this.list7 = pm7.split(',')
					}
					if (pm8 != null) {
						this.list8 = pm8.split(',')
					}
					if (pm9 != null) {
						this.list9 = pm9.split(',')
					}
					if (pm10 != null) {
						this.list10 = pm10.split(',')
					}
					// }
					// this.totalfx = res.data.result.total
					// console.log(this.ckanalysis, 'yhhhhhhhhh')
				})
			},
			// 桩基曲线
			zjmonitor() {
				// this.chartData1 = {
				// 	"categories": [
			
				// 	],
				// 	"series": [{
				// 		"name": "波幅",
				// 		"data": [
			
				// 		]
				// 	},
				// 	{
				// 		"name": "波速",
				// 		"data": [
			
				// 		]
				// 	}
				// 	]
				// }
				let data = {
					shebeino: this.zjlistdata.shebeino,
					liushuihao: this.zjlistdata.liushuihao,
					shizhuangno: this.zjlistdata.shizhuangno
				}
				this.$http.get(`/chaoshengbo/chaoshengboSybsj/list1`, {
					data
				}).then(res => {
					console.log(res.data.result, '桩基曲线')
					let zjcurve = res.data.result
					this.pmhs1 = []
					this.pmhs2 = []
					this.pmhs3 = []
					this.pmhs4 = []
					this.pmhs5 = []
					this.pmhs6 = []
					this.pmhs7 = []
					this.pmhs8 = []
					this.pmhs9 = []
					this.pmhs10 = []
			
			
					zjcurve.forEach((item, index) => {
						if (item.poumianhao1 !== null && item.poumianhao1 !== '') {
			
							this.pmhs1 = item.poumianhao1.split(',')
							console.log(this.pmhs1, 'ggggggg')
							this.chartData1.categories.push(item.shendu)
							// this.pmh1=this.pmhs1[0]
							this.chartData1.series[0].data.push((parseFloat(this.pmhs1[3])).toFixed(1))
							this.chartData1.series[1].data.push((parseFloat(this.pmhs1[1]) / parseFloat(
								this.pmhs1[2]) * 1000).toFixed(1))
						} else if (item.poumianhao2 !== null && item.poumianhao2 !== '') {
			
							this.pmhs2 = item.poumianhao2.split(',')
							this.chartData2.categories.push(item.shendu)
							this.chartData2.series[0].data.push((parseFloat(this.pmhs2[3])).toFixed(1))
							this.chartData2.series[1].data.push((parseFloat(this.pmhs2[1]) / parseFloat(
								this.pmhs1[2]) * 1000).toFixed(1))
						} else if (item.poumianhao3 !== null && item.poumianhao3 !== '') {
			
							this.pmhs3 = item.poumianhao3.split(',')
							this.chartData3.categories.push(item.shendu)
							this.chartData3.series[0].data.push((parseFloat(this.pmhs3[3])).toFixed(1))
							this.chartData3.series[1].data.push((parseFloat(this.pmhs2[3]) / parseFloat(
								this.pmhs3[2]) * 1000).toFixed(1))
						} else if (item.poumianhao4 !== null && item.poumianhao4 !== '') {
			
							this.pmhs4 = item.poumianhao4.split(',')
							this.chartData4.categories.push(item.shendu)
							this.chartData4.series[0].data.push((parseFloat(this.pmhs4[3])).toFixed(1))
							this.chartData4.series[1].data.push((parseFloat(this.pmhs4[3]) / parseFloat(
								this.pmhs4[2]) * 1000).toFixed(1))
						} else if (item.poumianhao5 !== null && item.poumianhao5 !== '') {
			
							this.pmhs5 = item.poumianhao5.split(',')
							this.chartData5.categories.push(item.shendu)
							this.chartData5.series[0].data.push((parseFloat(this.pmhs5[3])).toFixed(1))
							this.chartData5.series[1].data.push((parseFloat(this.pmhs5[3]) / parseFloat(
								this.pmhs5[2]) * 1000).toFixed(1))
						} else if (item.poumianhao6 !== null && item.poumianhao6 !== '') {
			
							this.pmhs6 = item.poumianhao6.split(',')
							this.chartData6.categories.push(item.shendu)
							this.chartData6.series[0].data.push((parseFloat(this.pmhs6[3])).toFixed(1))
							this.chartData6.series[1].data.push((parseFloat(this.pmhs6[3]) / parseFloat(
								this.pmhs6[2]) * 1000).toFixed(1))
						} else if (item.poumianhao7 !== null && item.poumianhao7 !== '') {
			
							this.pmhs7 = item.poumianhao7.split(',')
							this.chartData7.categories.push(item.shendu)
							this.chartData7.series[0].data.push(parseFloat(this.pmhs7[3]))
							this.chartData7.series[1].data.push((parseFloat(this.pmhs7[3]) / parseFloat(
								this.pmhs7[2]) * 1000).toFixed(1))
						} else if (item.poumianhao8 !== null && item.poumianhao8 !== '') {
			
							this.pmhs8 = item.poumianhao8.split(',')
							this.chartData8.categories.push(item.shendu)
							this.chartData8.series[0].data.push((parseFloat(this.pmhs8[3])).toFixed(1))
							this.chartData8.series[1].data.push((parseFloat(this.pmhs8[3]) / parseFloat(
								this.pmhs8[2]) * 1000).toFixed(1))
						} else if (item.poumianhao9 !== null && item.poumianhao9 !== '') {
			
							this.pmhs9 = item.poumianhao9.split(',')
							this.chartData9.categories.push(item.shendu)
							this.chartData9.series[0].data.push((parseFloat(this.pmhs9[3])).toFixed(1))
							this.chartData9.series[1].data.push((parseFloat(this.pmhs9[3]) / parseFloat(
								this.pmhs9[2]) * 1000).toFixed(1))
						} else if (item.poumianhao10 !== null && item.poumianhao10 !== '') {
			
							this.pmhs10 = item.poumianhao10.split(',')
							this.chartData10.categories.push(item.shendu)
							this.chartData10.series[0].data.push((parseFloat(this.pmhs10[3])).toFixed(1))
							this.chartData10.series[1].data.push((parseFloat(this.pmhs10[3]) / parseFloat(
								this.pmhs10[2]) * 1000).toFixed(1))
						}
			
					})
			
			
				})
			},
			// 桩基波列图
			Wavetrain() {
				this.detailList = []
				let params = {
					shebeino: this.zjlistdata.shebeino,
					liushuihao: this.zjlistdata.liushuihao,
					shizhuangno: this.zjlistdata.shizhuangno
				}
				this.$http.get(`/chaoshengbo/chaoshengboSybltsj/list1`, {
					params
				}).then(res => {
					console.log(res.data.result, '波列图')
					this.detailList = res.data.result
			
					// bolie.forEach(item => {
					// 	console.log(item)
					// 	if (item.imagedata !== null && item.imagedata == '') {
					// 		this.detailList.push({
					// 			icon: item.bolietu,
					// 			bh: item.poumianhao
					// 		})
					// 		console.log(this.detailList, 'ffffff')
					// 	}
					// })
			
				})
			},
			tabSelect1(e) {
				this.TabCur1 = e.currentTarget.dataset.id;
				if (this.TabCur1 == 0) {
					this.clickzj();
				} else if (this.TabCur1 == 1) {
					this.zjinfo()
				} else if (this.TabCur1 == 2) {
					this.zjmonitor()
				} else if (this.TabCur1 == 3) {
					this.zjmonitor()
				}
			},
			deviceType() {
				api.deviceType({
					params: {
						sys_depart_orgcode: this.orgcode,
						sbtypes: this.sbtype
					}
				}).then(res => {
					// console.log(res.data.result)
					let devicelist = res.data.result
					this.deviceNames = []
					this.devictype = []
					devicelist.forEach(e => {
						console.log(e)
						this.deviceNames.push(e.sbname)
						console.log(this.deviceNames,'dddd')
						this.devictype.push(e.sbjno)
						console.log(this.deviceNames,'fffff')
					})
				}).catch(e => {
					console.log("请求错误", e)
				})

			},
			// 设备名称
			PickerChange(e) {
				// console.log(e)
				// this.cbindex = -1
				this.index = e.detail.value
				this.imei = this.devictype[this.index]
				this.choosekey = 2
				this.choosevalue = this.devictype[this.index]
				this.deviceName = this.deviceNames[this.index]
				console.log(this.deviceName,'hhhh')
				this.begintime = ''
				this.endtime = ''

			},

			Choose(choosekey, choosevalue) {
				console.log(this.choosekey,this.choosevalue);
				// 设备名称
				if (this.choosekey == 2) {
					this.sbNumber = this.choosevalue
					console.log(this.sbNumber,'xxxx')
				}
				this.textlist()
				this.show = false
                  },
			// },
			textlist() {
				this.$http.get(`/chaoshengbo/chaoshengboSyjbsj/list`, {
					params: {
						column: 'id',
						order: 'desc',
						pageNo: this.pageNo,
						PageSize: 10,
						shebeino:this.sbNumber
					}
				}).then(res => {
					console.log(res.data.result.records, 'kkkkkkkkkkkkkkkkkk')
					if (res.data.success) {
						setTimeout(() => {
							// this.shows = false
							if (res.data.result.records.length == 0) {
								uni.showToast({
									title: '别下拉啦 没有更多数据了',
									icon: 'none'
								})
							}
							if (this.pageNo != 1) {
								this.shitilist = this.shitilist.concat(res.data.result.records)
							} else {
								this.shitilist = res.data.result.records
							}
						}, 300)
					}

				})
			},
			screen() {
				this.deviceName = ''
				this.show = true
			},
			cancel() {
				this.show = false
				
			},
			// handleOk() {
			// 	this.textlist()
			// 	this.show = false
			// },
			confirm() {
				this.textlist()
				this.show = false
			},
		}
	}
</script>

<style lang="scss" scoped>
	#home {
		// background-color: #F2F5FE;
		width: 100%;
		height: auto;
		background-color: #F2F5FE;
		// white-space: nowrap;
	}

	.bg {
		width: 100%;
		height: 150upx;
		background-color: #ffffff;
		position: fixed;
	}

	.header {
		width: 100%;
		height: 100upx;
		display: flex;

		// background-color: #506CE2;
		&-left {

			width: 20%;
			display: flex;
			justify-content: center;
			align-items: flex-end;

			&-tagpage {
				width: 34upx;
				height: 37upx;
				margin-left: 30upx;
			}

			&-screen {
				width: 34upx;
				height: 37upx;
				margin-left: 30upx;
			}

			// background-color: red;
		}

		&-center {
			width: 60%;
			// background-color: blue;
			display: flex;
			align-items: flex-end;
			overflow: hidden;
			white-space: nowrap;

			.center {
				width: 40%;
				text-align: center;
				font-size: 38upx;
				color: #FFFFFF;
			}

			.side {
				width: 30%;
				text-align: center;
				color: rgba(255, 255, 255, 0.4);
				// overflow: hidden;
				// white-space: nowrap;
			}
		}

		&-right {
			width: 20%;
			display: flex;
			justify-content: center;
			align-items: flex-end;

			&-tagpage {
				width: 34upx;
				height: 37upx;
				margin-left: 30upx;
			}

			&-screen {
				width: 34upx;
				height: 37upx;
				margin-left: 30upx;
			}

			// background-color: red;
		}
	}

	.newcolor {
		color: #000;
		font-size: 30upx;
	}

	.top-two {
		width: 700upx;
		height: 180upx;
		margin: 0 auto;
		// margin-top: 80upx;
		// border-radius: 16upx;
		// border:1px solid #333333;
		display: flex;
		justify-content: space-between;
		align-items: center;

		.left {
			width: 225upx;
			height: 150upx;
			border-radius: 10px;
			background-color: #ffffff;
			display: flex;
			justify-content: space-evenly;
			align-items: center;

			.left-img {
				width: 120upx;
				height: 110upx;
				// border: 1px solid blue;
				background: #cfe0fd;
				border-radius: 10px;

				.img {
					width: 60upx;
					height: 60upx;
					background-image: url(../../../static/shiti/five.png);
					// border: 1px solid #000;
					margin: 0 auto;
					line-height: 90upx;
					margin: 12px auto;
					background-size: 100% 92%;
					background-repeat: no-repeat;
				}
			}

			.left-text {
				width: 140upx;
				height: 100upx;
				// border: 1px solid #18BC37;
				color: #4C5971;
				text-align: center;
				font-size: 27upx;
				line-height: 50upx;
				font-family: 'DIN-Medium';

				.red {
					color: #FF233D;
					font-size: 50upx;
				}

				.orange {
					color: #FF8712;
					font-size: 50upx;
				}

				.green {
					color: #0FBF6E;
					font-size: 50upx;
				}

				.purple {
					color: #8333FA;
					font-size: 50upx;
				}

				.blue {
					color: #387FFD;
					font-size: 50upx;
				}

				.black {
					color: #333333;
					font-size: 50upx;
				}

			}
		}
	}

	.newcolor/deep/.cu-bar.fixed {
		position: fixed;
		width: 100%;
		top: 22px;
		z-index: 1024;
	}

	.scrolls-y {
		width: 100%;
		height: auto;
		// white-space: nowrap;
	}

	.section {
		width: 700upx;
		height: auto;
		border-radius: 10px;
		margin: 0 auto;
		background-color: #fff;

		.section-text {
			color: #9299A8;
			font-size: 30upx;
			padding: 15px 15px;
			line-height: 55upx;
			margin: 10px 0;

			span {
				color: #4C5971;
				font-size: 30upx;
				font-family: 'PingFang-SC-Medium';
				padding: 0 15upx;
			}
		}
	}

	.screen {
		position: fixed;
		background: rgba(000, 000, 000, .3);
		left: 0;
		top: 0;
		width: 100%;
		height: 100%;

		&-modal {
			background-color: #FFFFFF;
			position: absolute;
			top: 50%;
			left: (750upx - 690upx) / 2;
			width: 690upx;
			transform: translateY(-50%);
			box-shadow: 0 0 20px 0 rgba(0, 0, 0, 0.1);
			border-radius: 12upx;
			padding: 20upx;

			&-item {
				text-align: left;
				// width: 90%;
				margin-bottom: 30upx;

				&-name {
					color: #4C5971;
					font-size: 30upx;
					margin-bottom: 30upx;
				}

				&-input {}
			}

			&-btn {
				display: flex;
			}
		}

		.fixstyle {
			width: 320px;
			height: 37px;
			border: 1px solid #c4c9d0;
			background-color: #f5f7fa;
			border-radius: 5px;
			line-height: 37px;
			display: flex;

		}

	}

	.sectionwrap {
		width: 900px;
		height: auto;
		// overflow-y: auto;
		// border: 1px solid #ffaa00;

		.section-all {
			width:630rpx;
			height: auto;
			background-color: #fff;
			margin: 10px 0;
			// border: 1px solid #ED1C24;
			// margin: 15px auto;
			border-radius: 10px;

			.section-text {
				color: #9299A8;
				font-size: 30upx;
				padding: 0 15px;
				line-height: 55upx;
				margin: 10px 0;

				span {
					color: #4C5971;
					font-size: 30upx;
					font-family: 'PingFang-SC-Medium';
					padding: 10upx 15upx;
					// width:300rpx;
					// height:70rpx;
					//                background: #F6F9FC;
					// border-radius: 12px;
					// display: inline-block;

				}

				.title {
					width: 200rpx;
					height: 34px;
					// border: 1px solid #18BC37;
					display: flex;
					flex-direction: row;
					justify-content: space-between;
					align-items: center;

					.round {
						width: 6px;
						height: 17px;
						background: #387FFD;
						border-radius: 6px;
					}

					.title-font {
						font-size: 18px;
						font-family: ' PingFang SC';
						font-weight: bold;
						color: #333333;
					}
				}
			}
		}

	}

	.tablelist {
		width: 600rpx;
		height: auto;
		// overflow-y: auto;
		// border: 1px solid #5500ff;

	}

	.tables/deep/.u-modal {
		overflow-y: auto;
		height: 560px;
	}

	.tables/deep/.u-popup__content {
		background-color: #F2F5FE;
	}

	.title-fonts {
		font-size: 18px;
		font-family: ' PingFang SC';
		font-weight: bold;
		color: #333333;
		padding: 0 15px;
	}
</style>
