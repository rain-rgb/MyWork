<template>
	<view class="wrap">
		<cu-custom bgColor="bg-white" :isBack="true">
			<block slot="content">检验委托详情</block>
		</cu-custom>
		<view class="section">
			<view class="section-text">
				<view class="title">
					<view class="round"></view>
					<view class="title-font">基础信息</view>
				</view>
				<!-- <view class="">请选择施工部位:<span></span></view> -->
				<view class="">工程名称:<span>{{detailslist.projectname !==null?detailslist.projectname:'暂无数据'}}</span>
				</view>
				<view class="">施工部位:<span>{{detailslist.sgbwname!==null?detailslist.sgbwname:"暂无数据"}}</span></view>
				<view class="">
					试验类型:<span>{{detailslist.component_dictText!==null?detailslist.component_dictText:'暂无数据'}}</span>
				</view>
				<view class="">
					设备厂家:<span>{{detailslist.shebeichangjia_dictText!==null?detailslist.shebeichangjia_dictText:'暂无数据'}}</span>
				</view>
				<view class="">试验日期:<span>{{detailslist.zldate!==null?detailslist.zldate:"暂无数据"}}</span></view>
				<view class="">
					设备名称:<span>{{detailslist.shebeibianhao_dictText!==null?detailslist.shebeibianhao_dictText:'暂无数据'}}</span>
				</view>
			</view>
		</view>
		<view class="section">
			<view class="section-text">
				<view class="title">
					<view class="round"></view>
					<view class="title-font">检测试验</view>
				</view>
				<view v-show="detailslist.component == '钢筋保护层'" v-for="(item,index) in materilstable" :key="index">
					<view class="section-model" @click="clickJump(item.id,item.testid)">
						<view class="model-img">
							<!-- <view  style="item.jianyanstate ==0?'background-image:url('../../static/shiti/bhgy.png'); width: 45px;height: 45px;background-size: 100% 100%;':item.jianyanstate ==1?'background-image: url('../../static/shiti/yihg.png'); width: 45px;height: 45px;background-size: 100% 100%;':item.jianyanstate ==2?'background-image:url('../../static/shiti/yihg.png') ; width: 45px;height: 45px;background-size: 100% 100%;':''">
							    <image style="width: 45px;height: 45px;background-size: 100% 100%;" :src="item.jianyanstate == 0 ? img: item.jianyanstate == 1?imgtwo: item.jianyanstate ==2?imgthree:''" mode=""></image>
							</view> -->
							<view>
								<image style="width: 45px;height: 45px;background-size: 100% 100%;" :src="src" mode="">
								</image>
							</view>
						</view>
						<view class="model-text">
							<view class="tex-all">
								<view class="text-top">
									{{item.shebeiNo_dictText}}
								</view>
								<view class="text-bottom">
									{{item.targettype }}
								</view>
							</view>
						</view>
						<u-icon name="arrow-right" color="#c0c0c1" size="18">
						</u-icon>
					</view>
				</view>

				<view v-show="detailslist.component == '混凝土回弹'" v-for="(item,index) in gbilstable" :key="index">
					<view class="section-model" @click="clickht(item.id,item.testid)">
						<view class="model-img">
							<!-- <view  style="item.jianyanstate ==0?'background-image:url('../../static/shiti/bhgy.png'); width: 45px;height: 45px;background-size: 100% 100%;':item.jianyanstate ==1?'background-image: url('../../static/shiti/yihg.png'); width: 45px;height: 45px;background-size: 100% 100%;':item.jianyanstate ==2?'background-image:url('../../static/shiti/yihg.png') ; width: 45px;height: 45px;background-size: 100% 100%;':''">
							    <image style="width: 45px;height: 45px;background-size: 100% 100%;" :src="item.jianyanstate == 0 ? img: item.jianyanstate == 1?imgtwo: item.jianyanstate ==2?imgthree:''" mode=""></image>
							</view> -->
							<view>
								<image style="width: 45px;height: 45px;background-size: 100% 100%;" :src="src1" mode="">
								</image>
							</view>
						</view>
						<view class="model-text">
							<view class="tex-all">
								<view class="text-top">
									{{item.shebeiNo_dictText}}
								</view>
								<view class="text-bottom">
									{{item.detectionsurface }}
								</view>
							</view>
						</view>
						<u-icon name="arrow-right" color="#c0c0c1" size="18">
						</u-icon>
					</view>
				</view>

				<view v-show="detailslist.component == '成孔质量检测'" v-for="(item,index) in cktable" :key="index">
					<view class="section-model" @click="clickzl(item.id,item.testid)">
						<view class="model-img">
							<view>
								<image style="width: 45px;height: 45px;background-size: 100% 100%;" :src="src2" mode="">
								</image>
							</view>
						</view>
						<view class="model-text">
							<view class="tex-all">
								<view class="text-top">
									{{item.shebeino_dictText}}
								</view>
								<view class="text-bottom">
									{{item.pileno}}
								</view>
							</view>
						</view>
						<u-icon name="arrow-right" color="#c0c0c1" size="18">
						</u-icon>
					</view>
				</view>

				<view v-show="detailslist.component == '桩基超声波检测'" v-for="(item,index) in zjdata" :key="index">
					<view class="section-model" @click="clickzj(item.id,item.testid)">
						<view class="model-img">
							<view>
								<image style="width: 45px;height: 45px;background-size: 100% 100%;" :src="src3" mode="">
								</image>
							</view>
						</view>
						<view class="model-text">
							<view class="tex-all">
								<view class="text-top">
									{{item.shebeino_dictText}}
								</view>
								<view class="text-bottom">
									{{item.shizhuangleixing ==20?'原桩':item.shizhuangleixing ==21?'方桩':item.shizhuangleixing ==22?'地下连续墙':''}}
								</view>
							</view>
						</view>
						<u-icon name="arrow-right" color="#c0c0c1" size="18">
						</u-icon>
					</view>
				</view>


			</view>
			<view v-show="detailslist.component == '钢筋保护层'" class="tables">
				<u-modal :show="show" closeOnClickOverlay @close="() => show = false" :showConfirmButton="false"
					showCancelButton @cancel="cancel" :title="title" cancelColor="#000">
					<view class="sectionwrap">
						<view class="section-all">
							<view class="section-text">
								<view class="title">
									<!-- <view class="round"></view> -->
									<view class="title-font">基础信息</view>
								</view>
								<view class="">测点总数:<span>{{basicdata.zonecount}}</span></view>
								<view class="">合格率:<span>{{basicdata.passrate}}%</span></view>
								<view class="">检测位置:<span>{{basicdata.checklocation}}</span></view>
							</view>
						</view>
						<view class=" section-all">
							<view class="title">
								<!-- <view class="round"></view> -->
								<view class="title-fonts">钢保检测数据分析</view>
							</view>
							<view class="tablelist">
								<uni-table stripe emptyText="暂无更多数据">
									<!-- <view class="section-text"> -->
									<!-- <view class=""> -->
									<!-- </view> -->
									<!-- </view> -->
									<!-- 表头行 -->
									<uni-tr>
										<uni-th align="center">厚度</uni-th>
										<uni-th width="110" align="center">偏差</uni-th>
										<uni-th width="90" align="center">复测点数值</uni-th>
										<uni-th align="center">距离</uni-th>
										<uni-th width="110" align="center">是否合格</uni-th>
									</uni-tr>
									<!-- 表格数据行 -->
									<uni-tr v-for="(tablelist,index) in gbdata" :key="index">
										<uni-td align="center">{{tablelist.thickness}}</uni-td>
										<uni-td align="center">{{tablelist.thickness-tablelist.designthickness}}
										</uni-td>
										<uni-td align="center">{{tablelist.strthickness}}</uni-td>
										<uni-td align="center">{{tablelist.distance}}</uni-td>
										<uni-td align="center"
											:style="tablelist.flagpassrate == 1?'color:green':tablelist.flagpassrate == 0?'color:red':'color:orange'">
											{{tablelist.flagpassrate == 1?'合格':tablelist.flagpassrate == 0?'不合格':''}}
										</uni-td>
									</uni-tr>
								</uni-table>

							</view>

						</view>
						<!-- </view> -->
						<view style="padding: 5px 13px;">
							<uni-pagination :total="total" title="标题文字" :current="current" @change="change" />
						</view>


						<view class="charts-box section-all" style="width:300px;">

							<view class="title">

								<view class="title-fonts">钢保检测数据分析厚度</view>
							</view>
							<qiun-data-charts type="demotype" :chartData="chartData" :loadingType="10"
								:disableScroll="true" canvasId="" background="none" />
						</view>
					</view>

				</u-modal>
			</view>

			<view v-show="detailslist.component == '混凝土回弹'" class="tables">
				<u-modal :show="showht" closeOnClickOverlay @close="() => showht = false" :showConfirmButton="false"
					showCancelButton @cancel="cancelht" :title="title" cancelColor="#000">
					<view class="sectionwrap">
						<view class="section-all">
							<view class="section-text">
								<view class="title">
									<!-- <view class="round"></view> -->
									<view class="title-font">基础信息</view>
								</view>
								<view class="">构件部位:<span>{{htdata.place}}</span></view>
								<view class="">强度等级:<span>{{htdata.strength}}</span></view>
								<view class="">测区数量:<span>{{htdata.testingcount}}</span></view>
								<view class="">检测面:<span>{{htdata.detectionsurface}}</span></view>
								<view class="">标准碳化深度值:<span>{{htdata.carbonizedepth}}</span></view>
								<view class="">检测角度:<span>{{htdata.detectionangle}}</span></view>
							</view>
						</view>
						<view class=" section-all">
							<view class="title">
								<!-- <view class="round"></view> -->
								<view class="title-fonts">混凝土回弹检测数据分析</view>
							</view>
							<view class="tablelist">


								<uni-table border stripe emptyText="暂无更多数据">
									<!-- <view class="section-text"> -->
									<!-- <view class=""> -->
									<!-- </view> -->
									<!-- </view> -->
									<!-- 表头行 -->
									<uni-tr>
										<uni-th align="center">回弹平均值</uni-th>
										<uni-th align="center">测区</uni-th>
										<uni-th align="center">强度值</uni-th>
										<uni-th align="center">强度值</uni-th>
										<uni-th align="center">面修正值</uni-th>
										<uni-th align="center">角度修正值</uni-th>
										<uni-th align="center">浇筑面</uni-th>
										<uni-th align="center">浇筑面</uni-th>
										<uni-th align="center">碳化值</uni-th>
									</uni-tr>
									<!-- 表格数据行 -->
									<uni-tr v-for="(tablelist,index) in htlistdata" :key="index">
										<uni-td align="center">{{tablelist.average}}</uni-td>
										<uni-td align="center">
											<view v-for="(items,index) in tablelist.cequlist" :key="index">
												{{items}}
											</view>
										</uni-td>
										<uni-td align="center">{{tablelist.carbonize}}</uni-td>
										<uni-td align="center">{{tablelist.strcar}}</uni-td>
										<uni-td align="center">
											{{tablelist.calsurface}}
										</uni-td>
										<uni-td align="center">{{tablelist.calangle}}</uni-td>
										<uni-td align="center"
											:style="tablelist.pouringsurface ==0?'color:green':tablelist.pouringsurface ==1?'color:blue':'color:orange'">
											{{tablelist.pouringsurface ==0?'浇筑侧面':tablelist.pouringsurface ==1?'浇筑地面':''}}
										</uni-td>
										<uni-td align="center"
											:style="tablelist.surface ==1?'color:orange':tablelist.surface ==2?'color:blue':tablelist.surface ==3 ?'color:green':''">
											{{tablelist.surface == 1?'表面':tablelist.surface ==2?'底面':tablelist.surface==3?'侧面':''}}
										</uni-td>
										<uni-td align="center">{{tablelist.carbonization}}</uni-td>
									</uni-tr>
								</uni-table>
							</view>
							<view style="padding: 5px 13px;">
								<uni-pagination :total="totalht" title="标题文字" :current="currentht" @change="changeht" />
							</view>
						</view>
					</view>

				</u-modal>
			</view>


			<view v-show="detailslist.component == '成孔质量检测'" class="tables">
				<u-modal :show="showck" closeOnClickOverlay @close="() => showck = false" :showConfirmButton="false"
					showCancelButton @cancel="cancelck" :title="title" cancelColor="#000">
					<view class="sectionwrap">
						<view class="section-all">
							<view class="section-text">
								<view class="title">
									<!-- <view class="round"></view> -->
									<view class="title-font">基础信息</view>
								</view>
								<view class="">孔深(m):<span>{{ckdata.depth}}</span></view>
								<view class="">孔径(mm):<span>{{ckdata.diameter}}</span></view>
								<view class="">始测深度(m):<span>{{ckdata.begindepth}}</span></view>
								<view class="">标高(m):<span>{{ckdata.height}}</span></view>
								<view class="">管数:<span>{{ckdata.pipenums}}</span></view>
								<view class="">移距(mm):<span>{{ckdata.shift}}</span></view>
								<view class="">偏移角:<span>{{ckdata.angle}}</span></view>
							</view>
						</view>
						<view class="" style="width: 300px;">
							<scroll-view scroll-x class="nav bg-white">
								<view class="flex text-center">
									<view class="cu-item flex-sub" :class="0==TabCur?'text-blue cur':''"
										@tap="tabSelect" data-id="0">
										通道参数
									</view>
									<view class="cu-item flex-sub" :class="1==TabCur?'text-blue cur':''"
										@tap="tabSelect" data-id="1">
										孔径通道采样数据
									</view>
									<view class="cu-item flex-sub" :class="2==TabCur?'text-blue cur':''"
										@tap="tabSelect" data-id="2">
										孔径分析数据
									</view>
								</view>
							</scroll-view>
							<view v-if="TabCur==0" class="section-all">
								<!-- <view class="title">
								<!<view class="round"></view> -->
								<!-- 	<view class="title-fonts">通道参数</view>
							</view> -->
								<!-- <view class="pourstas"> -->

								<view class="tablelist">


									<uni-table stripe emptyText="暂无更多数据">
										<!-- <view class="section-text"> -->
										<!-- <view class=""> -->
										<!-- </view> -->
										<!-- </view> -->
										<!-- 表头行 -->
										<uni-tr>
											<uni-th align="center">通道号</uni-th>
											<uni-th align="center">状态</uni-th>
											<uni-th align="center">中心距(mm)</uni-th>
											<uni-th align="center">增益</uni-th>
											<uni-th align="center">延时(us)</uni-th>
											<uni-th align="center">校零时间(us)</uni-th>
										</uni-tr>
										<!-- 表格数据行 -->
										<uni-tr v-for="(tablelist,index) in ckvalue" :key="index">
											<uni-td align="center">{{tablelist.channelnum}}</uni-td>
											<uni-td align="center">
												<view
													:style="tablelist.status ==0?'color:red':tablelist.status ==1?'color:green':''">
													{{tablelist.status ==0?'未启用':tablelist.status ==1?'启用并显示':''}}
												</view>
											</uni-td>
											<uni-td align="center">{{tablelist.centerdistance}}</uni-td>
											<uni-td align="center">{{tablelist.gain}}</uni-td>
											<uni-td align="center">
												{{tablelist.delay}}
											</uni-td>
											<uni-td align="center">{{tablelist.zerotime}}</uni-td>
										</uni-tr>
									</uni-table>
								</view>
								<!-- </view> -->
								<view style="padding: 5px 13px;">
									<uni-pagination :total="totalck" title="标题文字" :current="currentck"
										@change="changeck" />
								</view>
							</view>

							<view v-if="TabCur==1" class=" section-all">
								<!-- <view class="title">
								<! <view class="round"></view> -->
								<!-- <view class="title-fonts">孔径通道采样数据</view>
							</view> -->
								<view class="tablelist">


									<uni-table stripe emptyText="暂无更多数据">
										<!-- <view class="section-text"> -->
										<!-- <view class=""> -->
										<!-- </view> -->
										<!-- </view> -->
										<!-- 表头行 -->
										<uni-tr>
											<uni-th align="center">通道号</uni-th>
											<uni-th align="center">深度(m)</uni-th>
											<uni-th align="center">声时(us)</uni-th>
											<uni-th align="center">泥浆声速(km/s)</uni-th>
											<uni-th align="center">波幅</uni-th>
											<uni-th align="center">方位角</uni-th>
											<uni-th align="center">半径(mm)</uni-th>
											<uni-th align="center">增益</uni-th>
											<uni-th align="center">延时(us)</uni-th>
											<uni-th align="center">增强声时(us)</uni-th>
											<uni-th align="center">修正距离(mm)</uni-th>
											<uni-th align="center">修正方向(us)</uni-th>
											<uni-th align="center">噪声档位</uni-th>
											<uni-th align="center">测点时间(s)</uni-th>
											<uni-th align="center">波形数值</uni-th>
										</uni-tr>
										<!-- 表格数据行 -->
										<uni-tr v-for="(tablelist,index) in cktongdao" :key="index">
											<uni-td align="center">{{tablelist.passway}}</uni-td>
											<uni-td align="center">{{tablelist.depth}}</uni-td>
											<uni-td align="center">{{tablelist.soundtime}}</uni-td>
											<uni-td align="center">{{tablelist.soundspeed}}</uni-td>
											<uni-td align="center">{{tablelist.amplitude}}</uni-td>
											<uni-td align="center">{{tablelist.azimuth}}</uni-td>
											<uni-td align="center">{{tablelist.radius}}</uni-td>
											<uni-td align="center">{{tablelist.gain}}</uni-td>
											<uni-td align="center">{{tablelist.delay}}</uni-td>
											<uni-td align="center">{{tablelist.enhancetime}}</uni-td>
											<uni-td align="center">{{tablelist.correctiondistance}}</uni-td>
											<uni-td align="center">{{tablelist.correctiondirection_dictText}}</uni-td>
											<uni-td align="center">{{tablelist.adjustinggear}}</uni-td>
											<uni-td align="center">{{tablelist.testtime}}</uni-td>
											<uni-td align="center">{{tablelist.wavedata}}</uni-td>
										</uni-tr>
									</uni-table>
								</view>
								<view style="padding: 5px 13px;">
									<uni-pagination :total="totaldata" title="标题文字" :current="currentdata"
										@change="changeckdata" />
								</view>
							</view>


							<view v-if="TabCur==2" class=" section-all">
								<!-- <view class="title">
								<! <view class="round"></view> -->
								<!-- <view class="title-fonts">孔径分析数据</view>
							</view> -->
								<view class="tablelist">


									<uni-table stripe emptyText="暂无更多数据">
										<!-- <view class="section-text"> -->
										<!-- <view class=""> -->
										<!-- </view> -->
										<!-- </view> -->
										<!-- 表头行 -->
										<uni-tr>
											<uni-th align="center">设计井深(m)</uni-th>
											<uni-th align="center">设计孔径(mm)</uni-th>
											<uni-th align="center">最大孔径(mm)</uni-th>
											<uni-th align="center">最小孔径(mm)</uni-th>
											<uni-th align="center">平均孔径(mm)</uni-th>
											<uni-th align="center">偏心距(mm)</uni-th>
											<uni-th align="center">沉渣厚度(m)</uni-th>
											<uni-th align="center">垂直度(%)</uni-th>
										</uni-tr>
										<!-- 表格数据行 -->
										<uni-tr v-for="(tablelist,index) in ckanalysis" :key="index">
											<uni-td align="center">{{tablelist.designdepth}}</uni-td>
											<uni-td align="center">{{tablelist.designdiameter}}</uni-td>
											<uni-td align="center">{{tablelist.maxdiameter}}</uni-td>
											<uni-td align="center">{{tablelist.mindiameter}}</uni-td>
											<uni-td align="center">{{tablelist.avgdiameter}}</uni-td>
											<uni-td align="center">{{tablelist.eccentricity}}</uni-td>
											<uni-td align="center">{{tablelist.thickness}}</uni-td>
											<uni-td align="center">{{tablelist.verticality}}</uni-td>
										</uni-tr>
									</uni-table>

								</view>
								<view style="padding: 5px 13px;">
									<uni-pagination :total="totalfx" title="标题文字" :current="currentfx"
										@change="changefx" />
								</view>
							</view>
						</view>
					</view>

				</u-modal>
			</view>

			<view v-show="detailslist.component == '桩基超声波检测'" class="tables">
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
							<view v-if="TabCur1==0" class=" section-all">
								<!-- <view class="title">
								<!<view class="round"></view> -->
								<!-- 	<view class="title-fonts">通道参数</view>
							</view> -->
								<view class="tablelist">


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
								</view>
								<!-- <view style="padding: 5px 13px;">
									<uni-pagination :total="totalck" title="标题文字" :current="currentck"
										@change="changeck" />
								</view> -->
							</view>
							<!-- 监测信息 -->
							<view v-if="TabCur1==1" class=" section-all">
								<view class="tablelist">


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
											<view class="" v-if="list1.length>1">
												<uni-td width="80" align="center" v-for="(item,index) in list1"
													:key="index">
													{{item}}
												</uni-td>
											</view>
											<view class="" v-if="list2.length>1">
												<uni-td width="80" align="center" v-for="(item,index) in list2"
													:key="index">{{item}}
												</uni-td>
											</view>
											<view class="" v-if="list3.length>1">
												<uni-td width="80" align="center" v-for="(item,index) in list3"
													:key="index">{{item}}
												</uni-td>
											</view>
											<view class="" v-if="list4.length>1">
												<uni-td width="80" align="center" v-for="(item,index) in list4"
													:key="index">{{item}}
												</uni-td>
											</view>
											<view class="" v-if="list5.length>1">
												<uni-td width="80" align="center" v-for="(item,index) in list5"
													:key="index">{{item}}
												</uni-td>
											</view>
											<view class="" v-if="list6.length>1">
												<uni-td width="80" align="center" v-for="(item,index) in list6"
													:key="index">{{item}}
												</uni-td>
											</view>
											<view class="" v-if="list7.length>1">
												<uni-td width="80" align="center" v-for="(item,index) in list7"
													:key="index">{{item}}
												</uni-td>
											</view>
											<view class="" v-if="list8.length>1">
												<uni-td width="80" align="center" v-for="(item,index) in list8"
													:key="index">{{item}}
												</uni-td>
											</view>
											<view class="" v-if="list9.length>1">
												<uni-td width="80" align="center" v-for="(item,index) in list9"
													:key="index">{{item}}
												</uni-td>
											</view>
											<view class="" v-if="list10.length>1">
												<uni-td width="80" align="center" v-for="(item,index) in list10"
													:key="index">{{item}}
												</uni-td>
											</view>
										</uni-tr>
									</uni-table>
								</view>
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
									<qiun-data-charts type="mix" :chartData="chartData2" :ontouch="true" :opts="{yAxis:{data:[{position: 'left',title: '波幅'},
											{position: 'right',title: '波速',textAlign: 'left'}]},
											enableScroll:true,
											xAxis:{itemCount:5,scrollShow:false,disableScroll:true}}" />
								</view>
								<view class="charts-box section-all" style="width:300px;"
									v-if="chartData3.categories.length>0">
									<span class="xhlist">剖面{{pmhs3[0]}}</span>
									<qiun-data-charts type="mix" :chartData="chartData3" :ontouch="true" :opts="{yAxis:{data:[{position: 'left',title: '波幅'},
											{position: 'right',title: '波速',textAlign: 'left'}]},
											enableScroll:true,
											xAxis:{itemCount:5,scrollShow:false,disableScroll:true}}" />
								</view>
								<view class="charts-box section-all" style="width:300px;"
									v-if="chartData4.categories.length>0">
									<span class="xhlist">剖面{{pmhs4[0]}}</span>
									<qiun-data-charts type="mix" :chartData="chartData4" :ontouch="true" :opts="{yAxis:{data:[{position: 'left',title: '波幅'},
											{position: 'right',title: '波速',textAlign: 'left'}]},
											enableScroll:true,
											xAxis:{itemCount:5,scrollShow:false,disableScroll:true}}" />
								</view>

								<view class="charts-box section-all" style="width:300px;"
									v-if="chartData5.categories.length>0">
									<span class="xhlist">剖面{{pmhs5[0]}}</span>
									<qiun-data-charts type="mix" :chartData="chartData5" :ontouch="true" :opts="{yAxis:{data:[{position: 'left',title: '波幅'},
											{position: 'right',title: '波速',textAlign: 'left'}]},
											enableScroll:true,
											xAxis:{itemCount:5,scrollShow:false,disableScroll:true}}" />
								</view>
								<view class="charts-box section-all" style="width:300px;"
									v-if="chartData6.categories.length>0">
									<span class="xhlist">剖面{{pmhs6[0]}}</span>
									<qiun-data-charts type="mix" :chartData="chartData6" :ontouch="true" :opts="{yAxis:{data:[{position: 'left',title: '波幅'},
											{position: 'right',title: '波速',textAlign: 'left'}]},
											enableScroll:true,
											xAxis:{itemCount:5,scrollShow:false,disableScroll:true}}" />
								</view>
								<view class="charts-box section-all" style="width:300px;"
									v-if="chartData7.categories.length>0">
									<span class="xhlist">剖面{{pmhs7[0]}}</span>
									<qiun-data-charts type="mix" :chartData="chartData7" :ontouch="true" :opts="{yAxis:{data:[{position: 'left',title: '波幅'},
											{position: 'right',title: '波速',textAlign: 'left'}]},
											enableScroll:true,
											xAxis:{itemCount:5,scrollShow:false,disableScroll:true}}" />
								</view>

								<view class="charts-box section-all" style="width:300px;"
									v-if="chartData8.categories.length>0">
									<span class="xhlist">剖面{{pmhs8[0]}}</span>
									<qiun-data-charts type="mix" :chartData="chartData8" :ontouch="true" :opts="{yAxis:{data:[{position: 'left',title: '波幅'},
											{position: 'right',title: '波速',textAlign: 'left'}]},
											enableScroll:true,
											xAxis:{itemCount:5,scrollShow:false,disableScroll:true}}" />
								</view>
								<view class="charts-box section-all" style="width:300px;"
									v-if="chartData9.categories.length>0">
									<span class="xhlist">剖面{{pmhs9[0]}}</span>
									<qiun-data-charts type="mix" :chartData="chartData9" :ontouch="true" :opts="{yAxis:{data:[{position: 'left',title: '波幅'},
											{position: 'right',title: '波速',textAlign: 'left'}]},
											enableScroll:true,
											xAxis:{itemCount:5,scrollShow:false,disableScroll:true}}" />
								</view>
								<view class="charts-box section-all" style="width:300px;"
									v-if="chartData10.categories.length>0">
									<span class="xhlist">剖面{{pmhs10[0]}}</span>
									<qiun-data-charts type="mix" :chartData="chartData10" :ontouch="true" :opts="{yAxis:{data:[{position: 'left',title: '波幅'},
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
								<!-- uniapp-image -->
								<image :showLoading="true" v-for="(item,index) in detailList" :key="index"
									:src="urls+'/chaoshengbo/chaoshengboSybltsj/list2?id='+item.id"
									@tap="previewImage(urls+'/chaoshengbo/chaoshengboSybltsj/list2?id='+item.id)"
									mode="widthFix">
								</image>
								<!-- u view u--image -->
								<!-- <u--image :showLoading="true" v-for="(item,index) in detailList" :key="index"
									:src="urls+'/chaoshengbo/chaoshengboSybltsj/list2?id='+item.id"
									@tap="previewImage(urls+'/chaoshengbo/chaoshengboSybltsj/list2?id='+item.id)"
									mode="widthFix"></u--image> -->
							</view>


						</view>
					</view>

				</u-modal>
			</view>



		</view>

	</view>
</template>

<script>
	import configService from '../../common/service/config.service.js'
	export default {
		name: "Entitydetectiondetils",
		components: {

		},
		data() {
			return {
				// pmh1:"",
				urls: '',
				TabCur: 0,
				TabCur1: 0,
				current: 1,
				currentht: 1,
				currentck: 1,
				currentdata: 1,
				currentfx: 1,
				total: 50,
				totalht: 50,
				totalck: 50,
				totaldata: 50,
				totalfx: 50,
				pageSize: 10,
				basicdata: '',
				htdata: '',
				ckdata: '',
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
				show: false,
				showht: false,
				showck: false,
				showzj: false,
				// offmodal:true,
				title: '检测试验结果',
				src: '../../static/label/gbht.png',
				src1: '../../static/label/lxlx.png',
				src2: '../../static/label/yjsjjc.png',
				src3: '../../static/label/zjjc.png',
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
				// chartData10: {
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
				// },
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
		onLoad(options) {
			this.detailslist = JSON.parse(options.item)
			console.log(this.detailslist);
			this.id = this.detailslist.uuid

		},
		created() {
			this.htlisttable()
			this.urls = configService.apiUrl
			console.log(configService, 'dddd')

		},
		updated() {
			// this.htlisttable()
		},
		methods: {
			previewImage(img) {
				let images = [];
				images.push(img);
				uni.previewImage({
					current: 0,
					urls: images,
				})
			},
			cancel() {
				this.show = false
			},
			cancelht() {
				this.showht = false
			},
			cancelck() {
				this.showck = false
			},
			cancelzj() {
				this.showzj = false
			},

			// 点击详情中弹出模态框
			clickJump(e, testid) {
				// console.log(e,testid)
				this.testid = testid
				this.$http.get(`/trgangjinbhcm/trGangjinbhcM/list`, {
					params: {
						id: e
					}
				}).then(res => {
					console.log(res.data.result.records[0], 'ssss')
					this.basicdata = res.data.result.records[0]
				})
				this.show = true
				this.gblist()
			},
			//点击详情中弹出模态框
			clickht(e, htid) {
				this.htid = htid
				console.log(e, htid, '混凝土回弹')
				this.showht = true
				this.$http.get(`/trhnthtm/trHnthtM/list`, {
					params: {
						id: e
					}
				}).then(res => {
					console.log(res.data.result.records[0], 'ddddd')
					this.htdata = res.data.result.records[0]
				})
				this.hntlist()
			},
			//点击成孔质量检测数据弹出模态框
			clickzl(e, CKid) {
				this.showck = true
				this.$http.get(`/kongjingbasicinfo/kongjingBasicinfo/list`, {
					params: {
						id: e
					}
				}).then(res => {
					this.ckdata = res.data.result.records[0]
					console.log(this.ckdata, 'dddd')
					// this.guid = res.data.result.records[0].guid

				})
				setTimeout(() => {
					this.cklist()
					this.ckdatalist()
					this.ckfenxi()
				}, 500)
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
				}, 1000)
			},
			// 详情中基础回弹数据
			htlisttable() {
				if (this.detailslist.component == '混凝土回弹') {
					this.$http.get(`/trhnthtm/trHnthtM/list`, {
						params: {
							code: this.id
						}
					}).then(res => {
						console.log(res, '混凝土回弹')
						if (res.data.result.records.length > 0) {
							this.gbilstable = res.data.result.records
						}
					})
				} else if (this.detailslist.component == '钢筋保护层') {
					// gjlisttable() {
					this.$http.get(`/trgangjinbhcm/trGangjinbhcM/list`, {
						params: {
							code: this.id
						}
					}).then(res => {
						console.log(res.data.result.records, 'ssss')
						if (res.data.result.records.length > 0) {
							this.materilstable = res.data.result.records
						}
					})
					// },
				} else if (this.detailslist.component == '成孔质量检测') {
					this.$http.get(`/kongjingbasicinfo/kongjingBasicinfo/list`, {
						params: {
							serialno: this.id
						}
					}).then(res => {
						console.log(res.data.result.records, 'cccc')
						if (res.data.result.records.length > 0) {
							this.cktable = res.data.result.records
						}
					})
				} else if (this.detailslist.component == '桩基超声波检测') {
					this.$http.get(`/chaoshengbo/chaoshengboSyjbsj/list`, {
						params: {
							liushuihao: this.id
						}
					}).then(res => {
						console.log(res, '桩基超声波检测')
						this.zjdata = res.data.result.records
					})
				}
			},
			// 钢保回弹数据
			gblist() {
				// console.log(pageindex,'pageindex')
				this.chartData.categories = []
				this.chartData.series[0].data = []
				let current = this.current
				let test = this.testid
				let data = {
					testid: test,
					pageNo: current
				}
				this.$http.get(`trgangjinbhcs/trGangjinbhcS/list`, {
					data
				}).then(res => {
					console.log(res, 'ffffffffff')
					this.gbdata = res.data.result.records
					this.gbdata.forEach((gblist, index) => {
						// console.log(gblist, index, 'huyuyyy')
						this.chartData.categories.push(index)
						this.chartData.series[0].data.push(gblist.thickness)
					})
					this.total = res.data.result.total
					// this.pageSize = res.data.result.size
				})
			},
			// 混凝土回弹
			hntlist() {
				let currentht = this.currentht
				let ht = this.htid
				let data = {
					testid: ht,
					pageNo: currentht
				}
				this.$http.get(`/trhnthts/trHnthtS/list1`, {
					data
				}).then(res => {
					// console.log(res.data.result.records, '回弹表格数据')
					this.htlistdata = res.data.result.records
					this.totalht = res.data.result.total
				})
			},
			//通道参数
			cklist() {
				// console.log(this.ckdata.guid, 'ddddd')
				let currentck = this.currentck
				let data = {
					guid: this.ckdata.guid,
					column: 'id',
					order: 'desc',
					pageNo: currentck
				}
				this.$http.get(`/kongjingtongdao/kongjingTongdao/list`, {
					data
				}).then(res => {
					// console.log(res, '通道参数')
					this.ckvalue = res.data.result.records
					this.totalck = res.data.result.total
				})
			},
			//孔径通道采样数据
			ckdatalist() {
				let current = this.currentdata
				let data = {
					basicinfoid: this.ckdata.basicinfoid,
					column: 'id',
					order: 'desc',
					pageNo: current
				}
				// pageNo:this.ipagination2.current
				this.$http.get(`/kongjingpointsdata/kongjingPointsdata/list`, {
					data
				}).then(res => {
					// console.log(res, '孔径通道采样数据')
					this.cktongdao = res.data.result.records
					this.totaldata = res.data.result.total
					// console.log(this.totaldata, 'yhhhhhhhhh')
				})
			},
			//孔径分析数据
			ckfenxi() {
				let current = this.currentfx
				let data = {
					basicinfoid: this.ckdata.basicinfoid,
					column: 'id',
					order: 'desc',
					pageNo: current
				}
				this.$http.get(`/kongjinganalysisdata/kongjingAnalysisdata/list`, {
					data
				}).then(res => {
					console.log(res, '孔径分析数据')
					this.ckanalysis = res.data.result.records
					this.totalfx = res.data.result.total
					// console.log(this.ckanalysis, 'yhhhhhhhhh')
				})
			},
			//桩基监测信息
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
						console.log(this.list2)
					}
					if (pm3 != null) {
						this.list3 = pm3.split(',')
						console.log(this.list3)
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
				})
			},
			// 获取当前页数的点击事件
			change(e) {
				this.current = e.current
				this.gblist(this.current)
			},
			changeht(e) {
				this.currentht = e.current
				this.hntlist(this.currentht)
			},
			changeck(e) {
				this.currentck = e.current
				this.cklist(this.currentck)
			},
			changeckdata(e) {
				this.currentdata = e.current
				this.ckdatalist(this.currentdata)
			},
			changefx(e) {
				this.currentfx = e.current
				this.ckfenxi(this.currentfx)
			},
			tabSelect(e) {
				console.log(e)
				this.TabCur = e.currentTarget.dataset.id;
				if (this.TabCur == 0) {
					this.cklist();
				} else if (this.TabCur == 1) {
					this.ckdatalist()
				} else if (this.TabCur == 2) {
					this.ckfenxi()
				}

				// this.scrollLeft = (e.currentTarget.dataset.id - 1) * 60
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
			}
		}
	}
</script>

<style lang="scss" scoped>
	.wrap {
		width: 100%;
		height: auto;
		background-color: #F2F5FE;
	}

	.section {
		width: 100%;
		height: auto;
		background-color: #fff;
		margin: 15px auto;
		border-radius: 10px;

		// display: flex;
		// flex-direction: row;
		.section-text {
			color: #9299A8;
			font-size: 30upx;
			padding: 0 15px;
			line-height: 55upx;
			// margin: 10px 0;

			span {
				color: #4C5971;
				font-size: 30upx;
				font-family: 'PingFang-SC-Medium';
				// padding: 10upx 15upx;
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

	.borderlist {
		border-radius: 10px;
		width: 94%;
		margin: 0 auto;
	}

	.section-model {
		width: 670upx;
		height: 70px;
		margin: 10px auto;
		border-radius: 4px;
		background-color: #F6F7FA;
		display: flex;
		flex-direction: row;
		justify-content: space-around;
		align-items: center;

		.model-img {
			width: 45px;
			height: 45px;
			// border: 1px solid #1CBBB4;
		}

		.model-text {
			width: 470upx;
			height: 42px;
			// border: 1px solid #F1A532;

			.tex-all {
				width: 470upx;
				height: 25px;
				line-height: 25px;

				.text-top {
					font-size: 14px;
					font-family: PingFang SC;
					font-weight: bold;
					color: #333333;
				}

				.text-bottom {
					color: #6e7176;

				}
			}

		}
	}

	.sectionwrap {
		width: 310px;
		height: auto;
		margin: 0 auto;
		// overflow-y: auto;
		// border: 1px solid #ffaa00;

		.section-all {
			width: 630rpx;
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
		width: 94%;
		height: auto;
		border-radius: 10px;
		// overflow-y: auto;
		margin: 0 auto;
		// border: 1px solid #ffaaff;

	}

	// .pourstas {
	// 	width:600upx;
	// 	height: auto;
	// 	margin: 0 auto;
	// 	margin-top: 30upx;
	// 	border-radius: 16upx;
	// 	// border: 1px solid #F1A532;
	// }
	.tables/deep/.u-modal__content {
		// padding: 12px 25px 25px 25px;
		width: 330px;
		// border: 1px solid blue;
		display: flex;
		flex-direction: row;
		margin: 0 auto;
		justify-content: center;
	}

	.tables/deep/.u-modal {
		overflow-y: auto;
		height: 560px;
		width: 340px !important;
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

	// .uni-system-preview-image {
	// 	background: #fff !important;
	// }
</style>
