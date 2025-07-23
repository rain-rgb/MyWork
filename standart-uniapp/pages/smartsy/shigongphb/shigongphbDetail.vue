<template>
	<view id="shigongphbdetail">
		<cu-custom bgColor="bg-white" :isBack="true">
			<block slot="content">配料单详情</block>
		</cu-custom>
		<view class="main">
			<view class="main-item">
				<view class="biaoqian"></view>
				<view class="mainnew">任务单基础信息</view>
			</view>
			<view class="main-item">
				<view class="main-item-name">
					任务单号：<span>{{model.renwudan}}</span>
				</view>
			</view>
			<view class="main-item">
				<view class="main-item-name">
					施工部位：<span>{{model.conspos}}</span>
				</view>
			</view>
			<view class="main-item">
				<view class="main-item-name">
					工程名称：<span>{{model.projname}}</span>
				</view>
			</view>
			<view class="main-item">
				<view class="main-item-name">
					任务方量：<span>{{model.mete}}</span>
				</view>
			</view>
			<view class="main-item">
				<view class="main-item-name">
					生产线：<span>{{model.station_dictText}}</span>
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
				<view class="main-item-name">
					理论配合比：<span>{{model.code1}}</span>
				</view>
			</view>
			<view class="main-item">
				<view class="main-item-name">
					设备名称：<span>{{model.shebeibianhao_dictText}}</span>
				</view>
			</view>
			<view class="main-item">
				<view class="main-item-name">
					强度等级：<span>{{model.betlev}}</span>
				</view>
			</view>
			<view class="main-item">
				<view class="main-item-name">
					坍落度：<span>{{model.lands}}</span>
				</view>
			</view>
			<view class="main-item">
				<view class="main-item-name">
					抗渗等级：<span>{{model.filter}}</span>
				</view>
			</view>
			<view class="main-item">
				<view class="main-item-name">
					抗冻等级：<span>{{model.freeze}}</span>
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
				<view class="main-item-name">
					制梁任务单：<span>{{model.zhiliangcode}}</span>
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
								<u--input placeholder="/" border="none" v-model="name1" disabled disabledColor="#fff">
								</u--input>
							</uni-th>
						</uni-tr>
						<uni-tr>
							<uni-th>
								<u--input placeholder="/" border="none" v-model="name2" disabled disabledColor="#fff">
								</u--input>
							</uni-th>
						</uni-tr>
						<uni-tr>
							<uni-th>
								<u--input placeholder="/" border="none" v-model="name3" disabled disabledColor="#fff">
								</u--input>
							</uni-th>
						</uni-tr>
						<uni-tr>
							<uni-th>
								<u--input placeholder="/" border="none" v-model="name4" disabled disabledColor="#fff">
								</u--input>
							</uni-th>
						</uni-tr>
						<uni-tr>
							<uni-th>
								<u--input placeholder="/" border="none" v-model="name5" disabled disabledColor="#fff">
								</u--input>
							</uni-th>
						</uni-tr>
						<uni-tr>
							<uni-th>
								<u--input placeholder="/" border="none" v-model="name6" disabled disabledColor="#fff">
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
											<u--input placeholder="/" border="none" v-model="model.lc1_dictText"
												disabled disabledColor="#fff">
											</u--input>
										</uni-th>
										<uni-th v-if="this.model.m13!=null">
											<u--input placeholder="/" border="none" v-model="model.lc13_dictText"
												disabled disabledColor="#fff">
											</u--input>
										</uni-th>
										<uni-th v-if="this.model.m2!=null">
											<u--input placeholder="/" border="none" v-model="model.lc2_dictText"
												disabled disabledColor="#fff">
											</u--input>
										</uni-th>
										<uni-th v-if="this.model.m3!=null">
											<u--input placeholder="/" border="none" v-model="model.lc3dictText" disabled
												disabledColor="#fff">
											</u--input>
										</uni-th>
										<uni-th v-if="this.model.m4!=null">
											<u--input placeholder="/" border="none" v-model="model.lc4_dictText"
												disabled disabledColor="#fff">
											</u--input>
										</uni-th>
										<uni-th v-if="this.model.m5!=null">
											<u--input placeholder="/" border="none" v-model="model.lc5_dictText"
												disabled disabledColor="#fff">
											</u--input>
										</uni-th>
										<uni-th v-if="this.model.m6!=null">
											<u--input placeholder="/" border="none" v-model="model.lc6_dictText"
												disabled disabledColor="#fff">
											</u--input>
										</uni-th>
										<uni-th v-if="this.model.m7!=null">
											<u--input placeholder="/" border="none" v-model="model.lc7_dictText"
												disabled disabledColor="#fff">
											</u--input>
										</uni-th>
										<uni-th v-if="this.model.m12!=null">
											<u--input placeholder="/" border="none" v-model="model.lc12_dictText"
												disabled disabledColor="#fff">
											</u--input>
										</uni-th>
										<uni-th v-if="this.model.m8!=null">
											<u--input placeholder="/" border="none" v-model="model.lc8_dictTect"
												disabled disabledColor="#fff">
											</u--input>
										</uni-th>
										<uni-th v-if="this.model.m9!=null">
											<u--input placeholder="/" border="none" v-model="model.lc9_dictText"
												disabled disabledColor="#fff">
											</u--input>
										</uni-th>
										<uni-th v-if="this.model.m10!=null">
											<u--input placeholder="/" border="none" v-model="model.lc10_dictText"
												disabled disabledColor="#fff">
											</u--input>
										</uni-th>
										<uni-th v-if="this.model.m11!=null">
											<u--input placeholder="/" border="none" v-model="model.lc11_dictText"
												disabled disabledColor="#fff">
											</u--input>
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
											<u--input placeholder="/" border="none" v-model="model.mc4" disabled
												disabledColor="#fff">
											</u--input>
										</uni-th>
										<uni-th v-if="this.model.m5!=null">
											<u--input placeholder="/" border="none" v-model="model.mc5" disabled
												disabledColor="#fff">
											</u--input>
										</uni-th>
										<uni-th v-if="this.model.m6!=null">
											<u--input placeholder="/" border="none" v-model="model.mc6" disabled
												disabledColor="#fff">
											</u--input>
										</uni-th>
										<uni-th v-if="this.model.m7!=null">
											<u--input placeholder="/" border="none" v-model="model.mc7" disabled
												disabledColor="#fff">
											</u--input>
										</uni-th>
										<uni-th v-if="this.model.m12!=null">
											<u--input placeholder="/" border="none" v-model="model.mc12" disabled
												disabledColor="#fff">
											</u--input>
										</uni-th>
										<uni-th v-if="this.model.m8!=null">
											<u--input placeholder="/" border="none" v-model="model.mc8" disabled
												disabledColor="#fff">
											</u--input>
										</uni-th>
										<uni-th v-if="this.model.m9!=null">
											<u--input placeholder="/" border="none" v-model="model.mc9" disabled
												disabledColor="#fff">
											</u--input>
										</uni-th>
										<uni-th v-if="this.model.m10!=null">
											<u--input placeholder="/" border="none" v-model="model.mc10" disabled
												disabledColor="#fff">
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
		<view
			style="display: flex; justify-content: center; margin-top: 60upx;padding-left: 4%;padding-right: 4%;margin-bottom: 50upx;">
			<view style="padding-right: 3%;width: 40%;">
				<u-button text="编辑" type="primary" @click="editsumit(JSON.stringify(model))" v-has="'shigongphb:edit'"></u-button>
			</view>
			<view style="padding-right: 3%;width: 40%;">
				<u-button text="复制录入" type="warning" @click="copysumit" v-has="'shigongphb:copy'"></u-button>
			</view>
		</view>
		<view>
			<u-modal style="text-align: center;font-size: 20px;" :show="copyshow" showCancelButton @cancel="copycancel"
				@confirm="copyconfirm">
				<view style="width: 30%;">
					<uni-table align="center">
						<uni-tr>
							<uni-th>材料</uni-th>
						</uni-tr>
						<uni-tr>
							<uni-th>
								<u--input placeholder="/" border="none" v-model="name1" disabled disabledColor="#fff">
								</u--input>
							</uni-th>
						</uni-tr>
						<uni-tr>
							<uni-th>
								<u--input placeholder="/" border="none" v-model="name2" disabled disabledColor="#fff">
								</u--input>
							</uni-th>
						</uni-tr>
						<uni-tr>
							<uni-th>
								<u--input placeholder="/" border="none" v-model="name3" disabled disabledColor="#fff">
								</u--input>
							</uni-th>
						</uni-tr>
						<uni-tr>
							<uni-th>
								<u--input placeholder="/" border="none" v-model="name4" disabled disabledColor="#fff">
								</u--input>
							</uni-th>
						</uni-tr>
						<uni-tr>
							<uni-th>
								<u--input placeholder="/" border="none" v-model="name5" disabled disabledColor="#fff">
								</u--input>
							</uni-th>
						</uni-tr>
						<uni-tr>
							<uni-th>
								<u--input placeholder="/" border="none" v-model="name6" disabled disabledColor="#fff">
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
											<u--input placeholder="/" border="none" v-model="model.lc1_dictText"
												disabled disabledColor="#fff">
											</u--input>
										</uni-th>
										<uni-th v-if="this.model.m13!=null">
											<u--input placeholder="/" border="none" v-model="model.lc13_dictText"
												disabled disabledColor="#fff">
											</u--input>
										</uni-th>
										<uni-th v-if="this.model.m2!=null">
											<u--input placeholder="/" border="none" v-model="model.lc2_dictText"
												disabled disabledColor="#fff">
											</u--input>
										</uni-th>
										<uni-th v-if="this.model.m3!=null">
											<u--input placeholder="/" border="none" v-model="model.lc3dictText" disabled
												disabledColor="#fff">
											</u--input>
										</uni-th>
										<uni-th v-if="this.model.m4!=null">
											<u--input placeholder="/" border="none" v-model="model.lc4_dictText"
												disabled disabledColor="#fff">
											</u--input>
										</uni-th>
										<uni-th v-if="this.model.m5!=null">
											<u--input placeholder="/" border="none" v-model="model.lc5_dictText"
												disabled disabledColor="#fff">
											</u--input>
										</uni-th>
										<uni-th v-if="this.model.m6!=null">
											<u--input placeholder="/" border="none" v-model="model.lc6_dictText"
												disabled disabledColor="#fff">
											</u--input>
										</uni-th>
										<uni-th v-if="this.model.m7!=null">
											<u--input placeholder="/" border="none" v-model="model.lc7_dictText"
												disabled disabledColor="#fff">
											</u--input>
										</uni-th>
										<uni-th v-if="this.model.m12!=null">
											<u--input placeholder="/" border="none" v-model="model.lc12_dictText"
												disabled disabledColor="#fff">
											</u--input>
										</uni-th>
										<uni-th v-if="this.model.m8!=null">
											<u--input placeholder="/" border="none" v-model="model.lc8_dictTect"
												disabled disabledColor="#fff">
											</u--input>
										</uni-th>
										<uni-th v-if="this.model.m9!=null">
											<u--input placeholder="/" border="none" v-model="model.lc9_dictText"
												disabled disabledColor="#fff">
											</u--input>
										</uni-th>
										<uni-th v-if="this.model.m10!=null">
											<u--input placeholder="/" border="none" v-model="model.lc10_dictText"
												disabled disabledColor="#fff">
											</u--input>
										</uni-th>
										<uni-th v-if="this.model.m11!=null">
											<u--input placeholder="/" border="none" v-model="model.lc11_dictText"
												disabled disabledColor="#fff">
											</u--input>
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
											<u--input placeholder="/" border="none" v-model="model.mc4" type="number"
												@input="onChange1">
											</u--input>
										</uni-th>
										<uni-th v-if="this.model.m5!=null">
											<u--input placeholder="/" border="none" v-model="model.mc5" type="number"
												@input="onChange2">
											</u--input>
										</uni-th>
										<uni-th v-if="this.model.m6!=null">
											<u--input placeholder="/" border="none" v-model="model.mc6" type="number"
												@input="onChange3">
											</u--input>
										</uni-th>
										<uni-th v-if="this.model.m7!=null">
											<u--input placeholder="/" border="none" v-model="model.mc7" type="number"
												@input="onChange4">
											</u--input>
										</uni-th>
										<uni-th v-if="this.model.m12!=null">
											<u--input placeholder="/" border="none" v-model="model.mc12" type="number"
												@input="onChange5">
											</u--input>
										</uni-th>
										<uni-th v-if="this.model.m8!=null">
											<u--input placeholder="/" border="none" v-model="model.mc8" type="number"
												@input="onChange6">
											</u--input>
										</uni-th>
										<uni-th v-if="this.model.m9!=null">
											<u--input placeholder="/" border="none" v-model="model.mc9" type="number"
												@input="onChange7">
											</u--input>
										</uni-th>
										<uni-th v-if="this.model.m10!=null">
											<u--input placeholder="/" border="none" v-model="model.mc10" type="number"
												@input="onChange8">
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
			</u-modal>
		</view>
	</view>
</template>

<script>
	import smartsyapi from '../../../api/smartsy.js'
	import dict from '../../component/dict/dict.vue'
	export default {
		components: {
			dict
		},
		data() {
			return {
				name: '',
				code: '',
				model: {},
				name1: '材料名称',
				name2: '料仓名称',
				name3: '理论用量',
				name4: '含水率(%)',
				name5: '含水量(kg)',
				name6: '施工用量',
				copyshow: false
			}
		},
		onLoad(options) {
			this.model = JSON.parse(options.json);
		},
		methods: {
			editsumit(e) {
				//console.log("e",e)
				uni.navigateTo({
					url: '/pages/smartsy/shigongphb/shigongphbAdd?json=' + e + '&titlesta=1'
				})
			},
			copysumit() {
				this.copyshow = true
			},
			copycancel() {
				this.copyshow = false
			},
			copyconfirm() {
				smartsyapi.shigongphbadd(this.model).then(res => {
					if (res.data.success) {
						uni.showToast({
							title: "配料单复制录入成功",
							icon: 'none'
						})
						setTimeout(() => {
							this.copyshow = false
							uni.redirectTo({
								url: '/pages/smartsy/shigongphb/shigongphbManage'
							});
						}, 500);
					}
				})
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
				// console.log("this.model.ru5",this.model.ru5)
				this.onwater()
				//console.log("ru11", this.model.ru11)
			},
			onChange3(e) {
				this.model.mc6 = e
				this.model.mcl6 = ((this.model.u6 * this.model.mc6) / 100).toFixed()
				this.model.ru6 = (this.model.u6 + Number(this.model.mcl6)).toFixed()
				// console.log("this.model.ru6",this.model.ru6)
				this.onwater()
			},
			onChange4(e) {
				this.model.mc7 = e
				this.model.mcl7 = ((this.model.u7 * this.model.mc7) / 100).toFixed()
				this.model.ru7 = (this.model.u7 + Number(this.model.mcl7)).toFixed()
				// console.log("this.model.ru7",this.model.ru7)
				this.onwater()
			},
			onChange5(e) {
				this.model.mc12 = e
				this.model.mcl12 = ((this.model.u12 * this.model.mc12) / 100).toFixed()
				this.model.ru12 = (this.model.u12 + Number(this.model.mcl12)).toFixed()
				// console.log("this.model.ru12",this.model.ru12)
				this.onwater()
			},
			onChange6(e) {
				this.model.mc8 = e
				this.model.mcl8 = ((this.model.u8 * this.model.mc8) / 100).toFixed(2)
				this.model.ru8 = (this.model.u8 + Number(this.model.mcl8)).toFixed(2)
				console.log("this.model.ru8",this.model.ru8)
				this.onwater()
			},
			onChange7(e) {
				this.model.mc9 = e
				this.model.mcl9 = ((this.model.u9 * this.model.mc9) / 100).toFixed(2)
				this.model.ru9 = (this.model.u9 + Number(this.model.mcl9)).toFixed(2)
				console.log("this.model.ru9",this.model.ru9)
				this.onwater()
			},
			onChange8(e) {
				this.model.mc10 = e
				this.model.mcl10 = ((this.model.u10 * this.model.mc10) / 100).toFixed(2)
				this.model.ru10 = (this.model.u10 + Number(this.model.mcl10)).toFixed(2)
				console.log("this.model.ru10",this.model.ru10)
				this.onwater()
			},
			onwater() {
				let data = Number(this.model.mcl5) + Number(this.model.mcl4) + Number(this.model
					.mcl6) + Number(this.model.mcl7) + Number(this.model.mcl8) + Number(this.model.mcl9) + Number(
					this.model.mcl10) + Number(this.model.mcl12)
				this.model.ru11 = (this.model.u11 - data).toFixed()
			},
		}
	}
</script>

<style lang="scss" scoped>
	#shigongphbdetail {
		width: 100%;
		background-color: #F3F5FE;

		.main {
			width: 690upx;
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
				font-size: 28upx;
				padding-top: 30upx;

				&-name {
					margin-left: 30upx;
					text-align: left;
					color: #9299A8;

					span {
						margin-right: 30upx;
						color: #4C5971;
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

		.progress {
			width: 690upx;
			height: 736upx;
			margin: 0 auto;
			margin-top: 30upx;
			border-radius: 16upx;
			background-color: #FFFFFF;
			padding-top: 30upx;
			padding-left: 30upx;
		}
	}
</style>
