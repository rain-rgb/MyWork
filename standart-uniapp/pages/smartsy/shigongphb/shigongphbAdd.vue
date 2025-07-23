<template>
	<view id="shigongphbadd">
		<cu-custom bgColor="bg-white" :isBack="true">
			<block slot="content">{{title}}</block>
		</cu-custom>
		<view>
			<view class="main">
				<view class="main-item">
					<view class="biaoqian"></view>
					<view class="mainnew">任务单基础信息</view>
				</view>
				<view class="main-item">
					<view class="main-item-name">任务单号：</view>
					<view class="main-item-input">
						<view>
							<picker @change="changeCode" :value="index" :range="codeData">
								<u--input placeholder="请选择任务单号" border="surround" v-model="codeData[index]" disabled
									suffixIcon="arrow-down">
								</u--input>
							</picker>
						</view>
					</view>
				</view>
				<view class="main-item">
					<view class="main-item-name">施工部位：</view>
					<view class="main-item-input">
						<view>
							<u--input placeholder="暂无" border="surround" v-model="model.conspos" disabled>
							</u--input>
						</view>
					</view>
				</view>
				<view class="main-item">
					<view class="main-item-name">工程名称：</view>
					<view class="main-item-input">
						<u--input placeholder="暂无" border="surround" v-model="model.projname" disabled></u--input>
					</view>
				</view>
				<view class="main-item">
					<view class="main-item-name">任务方量：</view>
					<view class="main-item-input">
						<u--input placeholder="暂无" border="surround" v-model="model.mete" disabled></u--input>
					</view>
				</view>
				<!-- <view class="main-item">
					<view class="main-item-name">生产线：</view>
					<view class="main-item-input">
						<u--input placeholder="暂无" border="surround" v-model="stations" disabled></u--input>
					</view>
				</view> -->
				<view class="main-item">
					<view class="main-item-name">强度等级：</view>
					<view class="main-item-input">
						<u--input placeholder="暂无" border="surround" v-model="model.betlev" disabled>
						</u--input>
					</view>
				</view>
				<view class="main-item"></view>
			</view>
			<view class="main">
				<view class="main-item">
					<view class="biaoqian"></view>
					<view class="mainnew">理论配合比基础信息</view>
				</view>
				<view class="main-item">
					<view class="main-item-name">理论配合比：</view>
					<view class="main-item-input">
						<view>
							<picker @change="changellCode" :value="indexll" :range="llcodeData">
								<u--input placeholder="请选择理论配合比" border="surround" v-model="llcodeData[indexll]"
									disabled suffixIcon="arrow-down">
								</u--input>
							</picker>
						</view>
					</view>
				</view>
				<view class="main-item">
					<view class="main-item-name">设备名称：</view>
					<view class="main-item-input">
						<u--input placeholder="暂无" border="surround" v-model="bhjname">
						</u--input>
					</view>
				</view>
				<view class="main-item">
					<view class="main-item-name">生产线：</view>
					<view class="main-item-input">
						<dict dictCode="station" title="请选择生产线" @choose="chooseStation">
						</dict>
					</view>
				</view>
				<view class="main-item">
					<view class="main-item-name">坍落度：</view>
					<view class="main-item-input">
						<u--input placeholder="暂无" border="surround" v-model="model.lands">
						</u--input>
					</view>
				</view>
				<view class="main-item">
					<view class="main-item-name">抗渗等级：</view>
					<view class="main-item-input">
						<u--input placeholder="暂无" border="surround" v-model="model.filter"></u--input>
					</view>
				</view>
				<view class="main-item">
					<view class="main-item-name">抗冻等级：</view>
					<view class="main-item-input">
						<u--input placeholder="暂无" border="surround" v-model="model.freeze"></u--input>
					</view>
				</view>
				<view class="main-item"></view>
			</view>
			<view class="main">
				<view class="main-item">
					<view class="biaoqian"></view>
					<view class="mainnew">制梁任务单信息</view>
				</view>
				<view class="main-item">
					<view class="main-item-name">制梁任务单：</view>
					<view class="main-item-input">
						<view>
							<picker @change="changezlCode" :value="indexzl" :range="zlcodeData">
								<u--input placeholder="请选择制梁任务单" border="surround" v-model="zlcodeData[indexzl]"
									disabled suffixIcon="arrow-down">
								</u--input>
							</picker>
						</view>
					</view>
				</view>
				<view class="main-item"></view>
			</view>
			<view class="main">
				<view class="main-item">
					<view class="biaoqian"></view>
					<view class="mainnew">施工配合比信息</view>
				</view>
				<view class="main-item">
					<view style="width: 30%;">
						<uni-table>
							<uni-tr>
								<uni-th>材料</uni-th>
							</uni-tr>
							<uni-tr>
								<uni-th>
									<u--input placeholder="/" border="none" v-model="name1" disabled
										disabledColor="#fff">
									</u--input>
								</uni-th>
							</uni-tr>
							<uni-tr>
								<uni-th>
									<u--input placeholder="/" border="none" v-model="name2" disabled
										disabledColor="#fff">
									</u--input>
								</uni-th>
							</uni-tr>
							<uni-tr>
								<uni-th>
									<u--input placeholder="/" border="none" v-model="name3" disabled
										disabledColor="#fff">
									</u--input>
								</uni-th>
							</uni-tr>
							<uni-tr>
								<uni-th>
									<u--input placeholder="/" border="none" v-model="name4" disabled
										disabledColor="#fff">
									</u--input>
								</uni-th>
							</uni-tr>
							<uni-tr>
								<uni-th>
									<u--input placeholder="/" border="none" v-model="name5" disabled
										disabledColor="#fff">
									</u--input>
								</uni-th>
							</uni-tr>
							<uni-tr>
								<uni-th>
									<u--input placeholder="/" border="none" v-model="name6" disabled
										disabledColor="#fff">
									</u--input>
								</uni-th>
							</uni-tr>
						</uni-table>
					</view>
					<view style="width: 70%;">
						<view>
							<scroll-view scroll-x="true">
								<view style="width: 2000upx;">
									<uni-table>
										<uni-tr>
											<uni-th v-if="this.model.m1!=null">水泥(1)</uni-th>
											<uni-th v-if="this.model.m13!=null">水泥(2)</uni-th>
											<uni-th v-if="this.model.m2!=null">粉煤灰</uni-th>
											<uni-th v-if="this.model.m3!=null">矿粉</uni-th>
											<uni-th v-if="this.model.m4!=null">骨料(1)</uni-th>
											<uni-th v-if="this.model.m5!=null">骨料(2)</uni-th>
											<uni-th v-if="this.model.m6!=null">骨料(3)</uni-th>
											<uni-th v-if="this.model.m7!=null">骨料(4)</uni-th>
											<uni-th v-if="this.model.m12!=null">骨料(5)</uni-th>
											<uni-th v-if="this.model.m8!=null">外加剂(1)</uni-th>
											<uni-th v-if="this.model.m9!=null">外加剂(2)</uni-th>
											<uni-th v-if="this.model.m10!=null">外加剂(3)</uni-th>
											<uni-th v-if="this.model.m11!=null">水</uni-th>
										</uni-tr>
										<uni-tr>
											<uni-th v-if="this.model.m1!=null">
												<u--input placeholder="/" border="none" v-model="model.m1" disabled
													disabledColor="#fff">
												</u--input>
											</uni-th>
											<uni-th v-if="this.model.m13!=null">
												<u--input placeholder="/" border="none" v-model="model.m13" disabled
													disabledColor="#fff">
												</u--input>
											</uni-th>
											<uni-th v-if="this.model.m2!=null">
												<u--input placeholder="/" border="none" v-model="model.m2" disabled
													disabledColor="#fff">
												</u--input>
											</uni-th>
											<uni-th v-if="this.model.m3!=null">
												<u--input placeholder="/" border="none" v-model="model.m3" disabled
													disabledColor="#fff">
												</u--input>
											</uni-th>
											<uni-th v-if="this.model.m4!=null">
												<u--input placeholder="/" border="none" v-model="model.m4" disabled
													disabledColor="#fff">
												</u--input>
											</uni-th>
											<uni-th v-if="this.model.m5!=null">
												<u--input placeholder="/" border="none" v-model="model.m5" disabled
													disabledColor="#fff">
												</u--input>
											</uni-th>
											<uni-th v-if="this.model.m6!=null">
												<u--input placeholder="/" border="none" v-model="model.m6" disabled
													disabledColor="#fff">
												</u--input>
											</uni-th>
											<uni-th v-if="this.model.m7!=null">
												<u--input placeholder="/" border="none" v-model="model.m7" disabled
													disabledColor="#fff">
												</u--input>
											</uni-th>
											<uni-th v-if="this.model.m12!=null">
												<u--input placeholder="/" border="none" v-model="model.m12" disabled
													disabledColor="#fff">
												</u--input>
											</uni-th>
											<uni-th v-if="this.model.m8!=null">
												<u--input placeholder="/" border="none" v-model="model.m8" disabled
													disabledColor="#fff">
												</u--input>
											</uni-th>
											<uni-th v-if="this.model.m9!=null">
												<u--input placeholder="/" border="none" v-model="model.m9" disabled
													disabledColor="#fff">
												</u--input>
											</uni-th>
											<uni-th v-if="this.model.m10!=null">
												<u--input placeholder="/" border="none" v-model="model.m10" disabled
													disabledColor="#fff">
												</u--input>
											</uni-th>
											<uni-th v-if="this.model.m11!=null">
												<u--input placeholder="/" border="none" v-model="model.m11" disabled
													disabledColor="#fff">
												</u--input>
											</uni-th>
										</uni-tr>
										<uni-tr>
											<uni-th v-if="this.model.m1!=null">
												<silo-selector ref="siloSelector" cailiaono="6" :shebeino="shebeino"
													title="请选择料仓" @choose="choose1" :siloDatas="lc1"></silo-selector>
											</uni-th>
											<uni-th v-if="this.model.m13!=null">
												<silo-selector ref="siloSelector" cailiaono="6" :shebeino="shebeino"
													title="请选择料仓" @choose="choose2" :siloDatas="lc13"></silo-selector>
											</uni-th>
											<uni-th v-if="this.model.m2!=null">
												<silo-selector ref="siloSelector" cailiaono="8" :shebeino="shebeino"
													title="请选择料仓" @choose="choose3" :siloDatas="lc2"></silo-selector>
											</uni-th>
											<uni-th v-if="this.model.m3!=null">
												<silo-selector ref="siloSelector" cailiaono="7" :shebeino="shebeino"
													title="请选择料仓" @choose="choose4" :siloDatas="lc3"></silo-selector>
											</uni-th>
											<uni-th v-if="this.model.m4!=null">
												<silo-selector ref="siloSelector" cailiaono="1,2,3,4"
													:shebeino="shebeino" title="请选择料仓" @choose="choose5"
													:siloDatas="lc4">
												</silo-selector>
											</uni-th>
											<uni-th v-if="this.model.m5!=null">
												<silo-selector ref="siloSelector" cailiaono="1,2,3,4"
													:shebeino="shebeino" title="请选择料仓" @choose="choose6"
													:siloDatas="lc5">
												</silo-selector>
											</uni-th>
											<uni-th v-if="this.model.m6!=null">
												<silo-selector ref="siloSelector" cailiaono="1,2,3,4"
													:shebeino="shebeino" title="请选择料仓" @choose="choose7"
													:siloDatas="lc6">
												</silo-selector>
											</uni-th>
											<uni-th v-if="this.model.m7!=null">
												<silo-selector ref="siloSelector" cailiaono="1,2,3,4"
													:shebeino="shebeino" title="请选择料仓" @choose="choose8"
													:siloDatas="lc7">
												</silo-selector>
											</uni-th>
											<uni-th v-if="this.model.m12!=null">
												<silo-selector ref="siloSelector" cailiaono="1,2,3,4"
													:shebeino="shebeino" title="请选择料仓" @choose="choose9"
													:siloDatas="lc12">
												</silo-selector>
											</uni-th>
											<uni-th v-if="this.model.m8!=null">
												<silo-selector ref="siloSelector" cailiaono="9" :shebeino="shebeino"
													title="请选择料仓" @choose="choose10" :siloDatas="lc8"></silo-selector>
											</uni-th>
											<uni-th v-if="this.model.m9!=null">
												<silo-selector ref="siloSelector" cailiaono="9" :shebeino="shebeino"
													title="请选择料仓" @choose="choose11" :siloDatas="lc9"></silo-selector>
											</uni-th>
											<uni-th v-if="this.model.m10!=null">
												<silo-selector ref="siloSelector" cailiaono="9" :shebeino="shebeino"
													title="请选择料仓" @choose="choose12" :siloDatas="lc10"></silo-selector>
											</uni-th>
											<uni-th v-if="this.model.m11!=null">
												<silo-selector ref="siloSelector" cailiaono="5" :shebeino="shebeino"
													title="请选择料仓" @choose="choose13" :siloDatas="lc11"></silo-selector>
											</uni-th>
										</uni-tr>
										<uni-tr>
											<uni-th v-if="this.model.m1!=null">
												<u--input placeholder="/" border="none" v-model="model.u1" disabled
													disabledColor="#fff">
												</u--input>
											</uni-th>
											<uni-th v-if="this.model.m13!=null">
												<u--input placeholder="/" border="none" v-model="model.u13" disabled
													disabledColor="#fff">
												</u--input>
											</uni-th>
											<uni-th v-if="this.model.m2!=null">
												<u--input placeholder="/" border="none" v-model="model.u2" disabled
													disabledColor="#fff">
												</u--input>
											</uni-th>
											<uni-th v-if="this.model.m3!=null">
												<u--input placeholder="/" border="none" v-model="model.u3" disabled
													disabledColor="#fff">
												</u--input>
											</uni-th>
											<uni-th v-if="this.model.m4!=null">
												<u--input placeholder="/" border="none" v-model="model.u4" disabled
													disabledColor="#fff">
												</u--input>
											</uni-th>
											<uni-th v-if="this.model.m5!=null">
												<u--input placeholder="/" border="none" v-model="model.u5" disabled
													disabledColor="#fff">
												</u--input>
											</uni-th>
											<uni-th v-if="this.model.m6!=null">
												<u--input placeholder="/" border="none" v-model="model.u6" disabled
													disabledColor="#fff">
												</u--input>
											</uni-th>
											<uni-th v-if="this.model.m7!=null">
												<u--input placeholder="/" border="none" v-model="model.u7" disabled
													disabledColor="#fff">
												</u--input>
											</uni-th>
											<uni-th v-if="this.model.m12!=null">
												<u--input placeholder="/" border="none" v-model="model.u12" disabled
													disabledColor="#fff">
												</u--input>
											</uni-th>
											<uni-th v-if="this.model.m8!=null">
												<u--input placeholder="/" border="none" v-model="model.u8" disabled
													disabledColor="#fff">
												</u--input>
											</uni-th>
											<uni-th v-if="this.model.m9!=null">
												<u--input placeholder="/" border="none" v-model="model.u9" disabled
													disabledColor="#fff">
												</u--input>
											</uni-th>
											<uni-th v-if="this.model.m10!=null">
												<u--input placeholder="/" border="none" v-model="model.u10" disabled
													disabledColor="#fff">
												</u--input>
											</uni-th>
											<uni-th v-if="this.model.m11!=null">
												<u--input placeholder="/" border="none" v-model="model.u11" disabled
													disabledColor="#fff">
												</u--input>
											</uni-th>
										</uni-tr>
										<uni-tr>
											<uni-th v-if="this.model.m1!=null">
												<u--input placeholder="/" border="none" disabled disabledColor="#fff">
												</u--input>
											</uni-th>
											<uni-th v-if="this.model.m13!=null">
												<u--input placeholder="/" border="none" disabled disabledColor="#fff">
												</u--input>
											</uni-th>
											<uni-th v-if="this.model.m2!=null">
												<u--input placeholder="/" border="none" disabled disabledColor="#fff">
												</u--input>
											</uni-th>
											<uni-th v-if="this.model.m3!=null">
												<u--input placeholder="/" border="none" disabled disabledColor="#fff">
												</u--input>
											</uni-th>
											<uni-th v-if="this.model.m4!=null">
												<u--input placeholder="/" border="none" v-model="model.mc4"
													type="digit" @input="onChange1">
												</u--input>
											</uni-th>
											<uni-th v-if="this.model.m5!=null">
												<u--input placeholder="/" border="none" v-model="model.mc5"
													type="digit" @input="onChange2">
												</u--input>
											</uni-th>
											<uni-th v-if="this.model.m6!=null">
												<u--input placeholder="/" border="none" v-model="model.mc6"
													type="digit" @input="onChange3">
												</u--input>
											</uni-th>
											<uni-th v-if="this.model.m7!=null">
												<u--input placeholder="/" border="none" v-model="model.mc7"
													type="digit" @input="onChange4">
												</u--input>
											</uni-th>
											<uni-th v-if="this.model.m12!=null">
												<u--input placeholder="/" border="none" v-model="model.mc12"
													type="digit" @input="onChange5">
												</u--input>
											</uni-th>
											<uni-th v-if="this.model.m8!=null">
												<u--input placeholder="/" border="none" v-model="model.mc8"
													type="digit" @input="onChange6">
												</u--input>
											</uni-th>
											<uni-th v-if="this.model.m9!=null">
												<u--input placeholder="/" border="none" v-model="model.mc9"
													type="digit" @input="onChange7">
												</u--input>
											</uni-th>
											<uni-th v-if="this.model.m10!=null">
												<u--input placeholder="/" border="none" v-model="model.mc10"
													type="digit" @input="onChange8">
												</u--input>
											</uni-th>
											<uni-th v-if="this.model.m11!=null">
												<u--input placeholder="/" border="none" disabled disabledColor="#fff">
												</u--input>
											</uni-th>
										</uni-tr>
										<uni-tr>
											<uni-th v-if="this.model.m1!=null">
												<u--input placeholder="/" border="none" disabled disabledColor="#fff">
												</u--input>
											</uni-th>
											<uni-th v-if="this.model.m13!=null">
												<u--input placeholder="/" border="none" disabled disabledColor="#fff">
												</u--input>
											</uni-th>
											<uni-th v-if="this.model.m2!=null">
												<u--input placeholder="/" border="none" disabled disabledColor="#fff">
												</u--input>
											</uni-th>
											<uni-th v-if="this.model.m3!=null">
												<u--input placeholder="/" border="none" disabled disabledColor="#fff">
												</u--input>
											</uni-th>
											<uni-th v-if="this.model.m4!=null">
												<u--input placeholder="/" border="none" v-model="model.mcl4" disabled
													disabledColor="#fff">
												</u--input>
											</uni-th>
											<uni-th v-if="this.model.m5!=null">
												<u--input placeholder="/" border="none" v-model="model.mcl5" disabled
													disabledColor="#fff">
												</u--input>
											</uni-th>
											<uni-th v-if="this.model.m6!=null">
												<u--input placeholder="/" border="none" v-model="model.mcl6" disabled
													disabledColor="#fff">
												</u--input>
											</uni-th>
											<uni-th v-if="this.model.m7!=null">
												<u--input placeholder="/" border="none" v-model="model.mcl7" disabled
													disabledColor="#fff">
												</u--input>
											</uni-th>
											<uni-th v-if="this.model.m12!=null">
												<u--input placeholder="/" border="none" v-model="model.mcl12" disabled
													disabledColor="#fff">
												</u--input>
											</uni-th>
											<uni-th v-if="this.model.m8!=null">
												<u--input placeholder="/" border="none" v-model="model.mcl8" disabled
													disabledColor="#fff">
												</u--input>
											</uni-th>
											<uni-th v-if="this.model.m9!=null">
												<u--input placeholder="/" border="none" v-model="model.mcl9" disabled
													disabledColor="#fff">
												</u--input>
											</uni-th>
											<uni-th v-if="this.model.m10!=null">
												<u--input placeholder="/" border="none" disabled disabledColor="#fff">
												</u--input>
											</uni-th>
											<uni-th v-if="this.model.m11!=null">
												<u--input placeholder="/" border="none" disabled disabledColor="#fff">
												</u--input>
											</uni-th>
										</uni-tr>
										<uni-tr>
											<uni-th v-if="this.model.m1!=null">
												<u--input placeholder="/" border="none" v-model="model.ru1" disabled
													disabledColor="#fff">
												</u--input>
											</uni-th>
											<uni-th v-if="this.model.m13!=null">
												<u--input placeholder="/" border="none" v-model="model.ru13" disabled
													disabledColor="#fff">
												</u--input>
											</uni-th>
											<uni-th v-if="this.model.m2!=null">
												<u--input placeholder="/" border="none" v-model="model.ru2" disabled
													disabledColor="#fff">
												</u--input>
											</uni-th>
											<uni-th v-if="this.model.m3!=null">
												<u--input placeholder="/" border="none" v-model="model.ru3" disabled
													disabledColor="#fff">
												</u--input>
											</uni-th>
											<uni-th v-if="this.model.m4!=null">
												<u--input placeholder="/" border="none" v-model="model.ru4" disabled
													disabledColor="#fff">
												</u--input>
											</uni-th>
											<uni-th v-if="this.model.m5!=null">
												<u--input placeholder="/" border="none" v-model="model.ru5" disabled
													disabledColor="#fff">
												</u--input>
											</uni-th>
											<uni-th v-if="this.model.m6!=null">
												<u--input placeholder="/" border="none" v-model="model.ru6" disabled
													disabledColor="#fff">
												</u--input>
											</uni-th>
											<uni-th v-if="this.model.m7!=null">
												<u--input placeholder="/" border="none" v-model="model.ru7" disabled
													disabledColor="#fff">
												</u--input>
											</uni-th>
											<uni-th v-if="this.model.m12!=null">
												<u--input placeholder="/" border="none" v-model="model.ru12" disabled
													disabledColor="#fff">
												</u--input>
											</uni-th>
											<uni-th v-if="this.model.m8!=null">
												<u--input placeholder="/" border="none" v-model="model.ru8" disabled
													disabledColor="#fff">
												</u--input>
											</uni-th>
											<uni-th v-if="this.model.m9!=null">
												<u--input placeholder="/" border="none" v-model="model.ru9" disabled
													disabledColor="#fff">
												</u--input>
											</uni-th>
											<uni-th v-if="this.model.m10!=null">
												<u--input placeholder="/" border="none" v-model="model.ru10" disabled
													disabledColor="#fff">
												</u--input>
											</uni-th>
											<uni-th v-if="this.model.m11!=null">
												<u--input placeholder="/" border="none" v-model="model.ru11" disabled
													disabledColor="#fff">
												</u--input>
											</uni-th>
										</uni-tr>
									</uni-table>
								</view>
							</scroll-view>
						</view>
					</view>
				</view>
				<view class="main-item"></view>
			</view>

		</view>
		<view class="btn">
			<u-button text="提交" type="primary" @click="sumit"></u-button>
		</view>
	</view>
</template>

<script>
	import equipment from '../../component/equipment/equipment.vue'
	import projectname from '../../component/projectname/projectname.vue'
	import MxDatePicker from "@/components/mx-datepicker/mx-datepicker.vue";
	import dict from '../../component/dict/dict.vue'
	import smartbh from '../../../api/smartbh.js'
	import smartsyapi from '../../../api/smartsy.js'
	import siloSelector from '../../component/siloSelector/siloSelector.vue'
	import nowtime from '@/common/util/nowtime.js'
	export default {
		components: {
			equipment,
			projectname,
			dict,
			MxDatePicker,
			siloSelector
		},
		data() {
			return {
				codeData: [],
				llcodeData: [],
				zlcodeData: [],
				index: -1,
				indexll: -1,
				indexzl: -1,
				title: '',
				jsons: null,
				rwdcode: '',
				llcode: '',
				model: {
					mcl4: 0,
					mcl5: 0,
					mcl6: 0,
					mcl7: 0,
					mcl12: 0,
					mcl8: 0,
					mcl9: 0,
					mcl10: 0,
					m1: null,
					m2: null,
					m3: null,
					m4: null,
					m5: null,
					m6: null,
					m7: null,
					m8: null,
					m9: null,
					m10: null,
					m11: null,
					m12: null,
					m13: null,
					conspos: '',
					projname: '',
					mete: '',
					station: '',
					betlev: ''
				},
				// stations: '',
				dataSource1: [],
				dataSource2: [],
				dataSource3: [],
				bhjname: '',
				zhiliangcode: '',
				name1: '材料名称',
				name2: '料仓名称',
				name3: '理论用量',
				name4: '含水率(%)',
				name5: '含水量(kg)',
				name6: '施工用量',
				shebeino: '',
				lc1: '',
				lc2: '',
				lc3: '',
				lc4: '',
				lc5: '',
				lc6: '',
				lc7: '',
				lc8: '',
				lc9: '',
				lc10: '',
				lc11: '',
				lc12: '',
				lc13: '',
				urls: {}
			}
		},
		onLoad(options) {
			this.getcodelist()
			this.getllcodelist()
			this.getzlcodelist()
			this.urls = {}
			//console.log("options", options)
			if (options.titlesta == 1) {
				this.title = '配料单编辑'
				this.urls = smartsyapi.shigongphbedit
				this.jsons = JSON.parse(options.json)
				if (this.jsons != null) {
					this.model = this.jsons
					// this.stations = this.jsons.station_dictText
					this.codeData[this.index] = this.jsons.renwudan
					this.llcodeData[this.indexll] = this.jsons.code1
					this.zlcodeData[this.indexzl] = this.jsons.zhiliangcode
					this.shebeino = this.jsons.shebeibianhao
					this.bhjname = this.jsons.shebeibianhao_dictText
					this.lc1 = this.model.lc1
					this.lc2 = this.model.lc2
					this.lc3 = this.model.lc3
					this.lc4 = this.model.lc4
					this.lc5 = this.model.lc5
					this.lc6 = this.model.lc6
					this.lc7 = this.model.lc7
					this.lc8 = this.model.lc8
					this.lc9 = this.model.lc9
					this.lc10 = this.model.lc10
					this.lc11 = this.model.lc11
					this.lc12 = this.model.lc12
					this.lc13 = this.model.lc13
				}
			} else {
				this.title = '配料单新增'
				this.urls = smartsyapi.shigongphbadd
				if (options.code != null) {
					this.getcodedata(options.code)
					this.codeData[this.index] = options.code
					this.model.renwudan = options.code
					this.getData(options.code)
				}
			}
		},
		methods: {
			sumit() {
				if (!this.model.code1) {
					uni.showToast({
						title: '请选择理论配合比号',
						icon: 'none'
					})
					return
				}
				if (!this.model.renwudan) {
					uni.showToast({
						title: '请选择任务单号',
						icon: 'none'
					})
					return
				}
				this.urls(this.model).then(res => {
					if (res.data.success) {
						uni.showToast({
							title: this.title + "成功",
							icon: 'none'
						})
						setTimeout(() => {
							uni.redirectTo({
								url: '/pages/smartsy/shigongphb/shigongphbManage'
							});
						}, 500);

					}
				})
			},
			getcodedata(e) {
				smartbh.pourlist2({
					params: {
						code: e
					}
				}).then(res => {
					if (res.data.success) {
						if (res.data.result.length > 0) {
							let data = res.data.result[0]
							this.model.conspos = data.conspos
							this.model.projname = data.projname
							this.model.mete = data.mete
							// this.model.station = data.station
							this.model.betlev = data.betlev
							// this.stations = data.station_dictText
						}
					}
				})
			},
			//生产线
			chooseStation(choosevalue) {
				
				this.model.station = choosevalue
			},
			changeCode(e) {
				this.index = e.detail.value
				this.rwdcode = this.codeData[this.index]
				// this.model.renwudan = this.rwdcode
				this.model.renwudan = this.rwdcode.slice(5,this.rwdcode.length)
				this.getcodedata(this.rwdcode.slice(5,this.rwdcode.length))
			 console.log("renwudan", this.rwdcode.slice(5,this.rwdcode.length))
			},
			changellCode(e) {
				this.indexll = e.detail.value
				this.llcode = this.llcodeData[this.indexll]
				this.model.code1 = this.llcode
				smartsyapi.llphbdataLists({
					params: {
						code: this.llcode
					}
				}).then(res => {
					if (res.data.result.records.length > 0) {
						let data = res.data.result.records[0]
						this.model.filter = data.filters
						this.model.freeze = data.freeze
						this.model.betlev = data.betlevel
						this.model.lands = data.lands
						this.bhjname = data.bhjno_dictText
						this.model.shebeibianhao = data.bhjno
						this.shebeino = data.bhjno
						//console.log("data", data)
						let uuid = data.uuid
						this.getlistdata(uuid)
					} else {
						console.log("暂无数据")
					}
				})
			},
			changezlCode(e) {
				this.indexzl = e.detail.value
				this.zhiliangcode = this.zlcodeData[this.indexzl]
				this.model.zhiliangcode = this.zhiliangcode
			},
			getcodelist() {
				this.codeData = []
				let params = {
					dattim_begin: nowtime.getNextPreDate(nowtime.date(), -2),
					dattim_end: nowtime.date()
				}
				smartsyapi.bhzrwdcodeList({
					params
				}).then(res => {
					if (res.data.success) {
						let data = res.data.result
						if (data.length > 0) {
							for (let i = 0; i < data.length; i++) {
								if(data[i].status>1){
									this.codeData.push("(已配料)"+data[i].code)
								}else{
									this.codeData.push("(未配料)"+data[i].code)
								}
								
							}
							//console.log("res", res)
						}
					}
				})
			},
			getllcodelist() {
				this.llcodeData = []
				smartsyapi.llphbLists().then(res => {
					if (res.data.success) {
						let data = res.data.result
						if (data.length > 0) {
							for (let i = 0; i < data.length; i++) {
								this.llcodeData.push(data[i].code)
							}
							//console.log("llphbres", res)
						}
					}
				})
			},
			getzlcodelist() {
				this.zllcodeData = []
				smartsyapi.zhilaingList({
					params: {
						sys_depart_orgcode: this.$store.getters.orgcode,
					}
				}).then(res => {
					if (res.data.success) {
						let data = res.data.result
						if (data.length > 0) {
							for (let i = 0; i < data.length; i++) {
								this.zlcodeData.push(data[i].code)
							}
							//console.log("zlphbres", res)
						}
					}
				})
			},
			getlistdata(uuid) {
				//console.log("理论配合比材料信息uuid", uuid)
				// this.listdata = []
				this.dataSource1 = []
				this.dataSource2 = []
				this.dataSource3 = []
				this.model.m1 = null
				this.model.m2 = null
				this.model.m3 = null
				this.model.m4 = null
				this.model.m5 = null
				this.model.m6 = null
				this.model.m7 = null
				this.model.m8 = null
				this.model.m9 = null
				this.model.m10 = null
				this.model.m11 = null
				this.model.m12 = null
				this.model.m13 = null
				smartsyapi.llphbdetailLists({
					params: {
						id: uuid
					}
				}).then(res => {
					if (res.data.result.length > 0) {
						// this.listdata = res.data.result
						let data = res.data.result
						//console.log("data原材料", data)
						for (let i = 0; i < data.length; i++) {
							if (data[i].materialTypes == 6) { //水泥
								this.dataSource1.push({
									materialname: data[i].materialname,
									amount: data[i].amount
								})
								switch (this.dataSource1.length) {
									case 1:
										this.model.m1 = this.dataSource1[0].materialname
										this.model.u1 = this.dataSource1[0].amount
										this.model.ru1 = this.dataSource1[0].amount
										break
									case 2:
										this.model.m1 = this.dataSource1[0].materialname
										this.model.u1 = this.dataSource1[0].amount
										this.model.ru1 = this.dataSource1[0].amount
										this.model.m13 = this.dataSource1[1].materialname
										this.model.u13 = this.dataSource1[1].amount
										this.model.ru13 = this.dataSource1[1].amount
										break
								}
							}
							if (data[i].materialTypes == 8) { //粉煤灰
								this.model.m2 = data[i].materialname
								this.model.u2 = data[i].amount
								this.model.ru2 = data[i].amount
							}
							if (data[i].materialTypes == 7) { //矿粉
								this.model.m3 = data[i].materialname
								this.model.u3 = data[i].amount
								this.model.ru3 = data[i].amount
							}
							if (data[i].materialTypes == 1 || data[i].materialTypes == 2 || data[i]
								.materialTypes == 3 || data[i].materialTypes == 4) { //骨料
								this.dataSource2.push({
									materialname: data[i].materialname,
									amount: data[i].amount
								})
								switch (this.dataSource2.length) {
									case 1:
										this.model.m4 = this.dataSource2[0].materialname
										this.model.u4 = this.dataSource2[0].amount
										this.model.ru4 = this.dataSource2[0].amount
										break
									case 2:
										this.model.m4 = this.dataSource2[0].materialname
										this.model.u4 = this.dataSource2[0].amount
										this.model.ru4 = this.dataSource2[0].amount
										this.model.m5 = this.dataSource2[1].materialname
										this.model.u5 = this.dataSource2[1].amount
										this.model.ru5 = this.dataSource2[1].amount
										break
									case 3:
										this.model.m4 = this.dataSource2[0].materialname
										this.model.u4 = this.dataSource2[0].amount
										this.model.ru4 = this.dataSource2[0].amount
										this.model.m5 = this.dataSource2[1].materialname
										this.model.u5 = this.dataSource2[1].amount
										this.model.ru5 = this.dataSource2[1].amount
										this.model.m6 = this.dataSource2[2].materialname
										this.model.u6 = this.dataSource2[2].amount
										this.model.ru6 = this.dataSource2[2].amount
										break
									case 4:
										this.model.m4 = this.dataSource2[0].materialname
										this.model.u4 = this.dataSource2[0].amount
										this.model.ru4 = this.dataSource2[0].amount
										this.model.m5 = this.dataSource2[1].materialname
										this.model.u5 = this.dataSource2[1].amount
										this.model.ru5 = this.dataSource2[1].amount
										this.model.m6 = this.dataSource2[2].materialname
										this.model.u6 = this.dataSource2[2].amount
										this.model.ru6 = this.dataSource2[2].amount
										this.model.m7 = this.dataSource2[3].materialname
										this.model.u7 = this.dataSource2[3].amount
										this.model.ru7 = this.dataSource2[3].amount
										break
									case 5:
										this.model.m4 = this.dataSource2[0].materialname
										this.model.u4 = this.dataSource2[0].amount
										this.model.ru4 = this.dataSource2[0].amount
										this.model.m5 = this.dataSource2[1].materialname
										this.model.u5 = this.dataSource2[1].amount
										this.model.ru5 = this.dataSource2[1].amount
										this.model.m6 = this.dataSource2[2].materialname
										this.model.u6 = this.dataSource2[2].amount
										this.model.ru6 = this.dataSource2[2].amount
										this.model.m7 = this.dataSource2[3].materialname
										this.model.u7 = this.dataSource2[3].amount
										this.model.ru7 = this.dataSource2[3].amount
										this.model.m12 = this.dataSource2[4].materialname
										this.model.u12 = this.dataSource2[4].amount
										this.model.ru12 = this.dataSource2[4].amount
										break
								}
							}
							if (data[i].materialTypes == 9) { //外加剂
								this.dataSource3.push({
									materialname: data[i].materialname,
									amount: data[i].amount
								})
								switch (this.dataSource3.length) {
									case 1:
										this.model.m8 = this.dataSource3[0].materialname
										this.model.u8 = this.dataSource3[0].amount
										this.model.ru8 = this.dataSource3[0].amount
										break
									case 2:
										this.model.m8 = this.dataSource3[0].materialname
										this.model.u8 = this.dataSource3[0].amount
										this.model.ru8 = this.dataSource3[0].amount
										this.model.m9 = this.dataSource3[1].materialname
										this.model.u9 = this.dataSource3[1].amount
										this.model.ru9 = this.dataSource3[1].amount
										break
									case 3:
										this.model.m8 = this.dataSource3[0].materialname
										this.model.u8 = this.dataSource3[0].amount
										this.model.ru8 = this.dataSource3[0].amount
										this.model.m9 = this.dataSource3[1].materialname
										this.model.u9 = this.dataSource3[1].amount
										this.model.ru9 = this.dataSource3[1].amount
										this.model.m10 = this.dataSource3[2].materialname
										this.model.u10 = this.dataSource3[2].amount
										this.model.ru10 = this.dataSource3[2].amount
										break;
								}
							}
							if (data[i].materialTypes == 5) {
								this.model.m11 = data[i].materialname
								this.model.u11 = data[i].amount
								this.model.ru11 = data[i].amount
							}
						}
					} else {
						console.log("暂无数据=====")
					}
				})
			},
			choose1(e) {
				this.model.lc1 = e
				//console.log("lc1", this.model.lc1)
			},
			choose2(e) {
				this.model.lc13 = e
			},
			choose3(e) {
				this.model.lc2 = e
			},
			choose4(e) {
				this.model.lc3 = e
			},
			choose5(e) {
				this.model.lc4 = e
			},
			choose6(e) {
				this.model.lc5 = e
			},
			choose7(e) {
				this.model.lc6 = e
			},
			choose8(e) {
				this.model.lc7 = e
			},
			choose9(e) {
				this.model.lc12 = e
			},
			choose10(e) {
				this.model.lc8 = e
			},
			choose11(e) {
				this.model.lc9 = e
			},
			choose12(e) {
				this.model.lc10 = e
			},
			choose13(e) {
				this.model.lc11 = e
			},
			onChange1(e) {
				this.model.mc4 = e
				this.model.mcl4 = ((this.model.u4 * this.model.mc4) / 100).toFixed()
				this.model.ru4 = (this.model.u4 + Number(this.model.mcl4)).toFixed()
				//console.log("e", e)
				// console.log("ru4", this.model.ru4)
				this.onwater()
			},
			onChange2(e) {
				this.model.mc5 = e
				this.model.mcl5 = ((this.model.u5 * this.model.mc5) / 100).toFixed()
				this.model.ru5 = (this.model.u5 + Number(this.model.mcl5)).toFixed()
				this.onwater()
				//console.log("ru11", this.model.ru11)
			},
			onChange3(e) {
				this.model.mc6 = e
				this.model.mcl6 = ((this.model.u6 * this.model.mc6) / 100).toFixed()
				this.model.ru6 = (this.model.u6 + Number(this.model.mcl6)).toFixed()
				this.onwater()
			},
			onChange4(e) {
				this.model.mc7 = e
				this.model.mcl7 = ((this.model.u7 * this.model.mc7) / 100).toFixed()
				this.model.ru7 = (this.model.u7 + Number(this.model.mcl7)).toFixed()
				this.onwater()
			},
			onChange5(e) {
				this.model.mc12 = e
				this.model.mcl12 = ((this.model.u12 * this.model.mc12) / 100).toFixed()
				this.model.ru12 = (this.model.u12 + Number(this.model.mcl12)).toFixed()
				this.onwater()
			},
			onChange6(e) {
				this.model.mc8 = e
				this.model.mcl8 = ((this.model.u8 * this.model.mc8) / 100).toFixed(2)
				this.model.ru8 = (this.model.u8 + Number(this.model.mcl8)).toFixed(2)
				this.onwater()
			},
			onChange7(e) {
				this.model.mc9 = e
				this.model.mcl9 = ((this.model.u9 * this.model.mc9) / 100).toFixed(2)
				this.model.ru9 = (this.model.u9 + Number(this.model.mcl9)).toFixed(2)
				this.onwater()
			},
			onChange8(e) {
				this.model.mc10 = e
				this.model.mcl10 = ((this.model.u10 * this.model.mc10) / 100).toFixed(2)
				this.model.ru10 = (this.model.u10 + Number(this.model.mcl10)).toFixed(2)
				this.onwater()
			},
			onwater() {
				let data = Number(this.model.mcl5) + Number(this.model.mcl4) + Number(this.model
					.mcl6) + Number(this.model.mcl7) + Number(this.model.mcl8) + Number(this.model.mcl9) + Number(
					this.model.mcl10) + Number(this.model.mcl12)
				this.model.ru11 = (this.model.u11 - data).toFixed()
			},
			sumit() {
				if (!this.model.code1) {
					uni.showToast({
						title: '请选择理论配合比号',
						icon: 'none'
					})
					return
				}
				if (!this.model.renwudan) {
					uni.showToast({
						title: '请选择任务单号',
						icon: 'none'
					})
					return
				}
				this.$tip.loading();
				this.urls(this.model).then(res => {
					this.$tip.loaded();
					if (res.data.success) {
						uni.showToast({
							title: this.title + "成功",
							icon: 'none'
						})
						setTimeout(() => {
							uni.redirectTo({
								url: '/pages/smartsy/shigongphb/shigongphbManage'
							});
						}, 500);

					}
				})
			},
			getData(code) {
				smartsyapi.shigongphblist({
					params: {
						renwudan: code,
						column: 'id',
						order: 'desc',
					}
				}).then(res => {
					if (res.data.success) {
						if (res.data.result.records.length > 0) {
							this.model = res.data.result.records[0]
							this.llcodeData[this.indexll] = this.model.code1
							this.zlcodeData[this.indexzl] = this.model.zhiliangcode
							// this.stations = this.model.station_dictText
							this.shebeino = this.model.shebeibianhao
							this.bhjname = this.model.shebeibianhao_dictText
							this.lc1 = this.model.lc1
							this.lc2 = this.model.lc2
							this.lc3 = this.model.lc3
							this.lc4 = this.model.lc4
							this.lc5 = this.model.lc5
							this.lc6 = this.model.lc6
							this.lc7 = this.model.lc7
							this.lc8 = this.model.lc8
							this.lc9 = this.model.lc9
							this.lc10 = this.model.lc10
							this.lc11 = this.model.lc11
							this.lc12 = this.model.lc12
							this.lc13 = this.model.lc13
							//console.log("model",this.model)
						}
					}
				})
			}
		}
	}
</script>

<style lang="scss" scoped>
	#shigongphbadd {
		width: 100%;
		// height: 100vh;
		background-color: #F3F5FE;

		.main {
			width: 690upx;
			// height: 754upx;
			margin: 0 auto;
			margin-top: 30upx;
			background-color: white;
			border-radius: 16upx;

			.mainnew {
				color: #333333;
				font-size: 30upx;
				font-weight: bold;
			}

			&-item {
				display: flex;
				align-items: center;
				padding-top: 30upx;

				&-name {
					flex: 3;
					font-size: 30upx;
					text-align: right;
					color: #4C5971;
				}

				&-input {
					flex: 8;
					margin-right: 30upx;
					background-color: #F6F9FC;
					border-radius: 10upx;

					&-button {
						background-color: #F6F9FC;
					}

					&-text {
						padding: 6px 9px;
						font-size: 32rpx;
						margin-right: 30upx;
						background-color: #F6F9FC;
					}
				}
			}
		}

		.biaoqian {
			width: 12upx;
			height: 34upx;
			border-radius: 6upx;
			margin: 0 30upx;
			background-color: #4A7FFF;
		}

		.btn {
			width: 690upx;
			height: 100upx;
			margin: 0 auto;
			margin-top: 30upx;
		}
	}
</style>