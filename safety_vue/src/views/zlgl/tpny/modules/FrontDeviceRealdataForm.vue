<template>
  <div>
    <a-row :gutter="24" style="padding-bottom: 5px">
      <a-col :span="6">
        <j-date placeholder="开始时间" v-model="datatime_begin" :value="datatime_begin" :showTime="true"  dateFormat="YYYY-MM-DD HH:mm:ss" />
      </a-col>
      <a-col :span="6">
        <j-date placeholder="结束时间" v-model="datatime_end" :value="datatime_end" :showTime="true" dateFormat="YYYY-MM-DD HH:mm:ss" />
      </a-col>
      <a-col :span="6">
        <a-button type="primary" @click="Query" icon="search">查询</a-button>
      </a-col>
    </a-row>
    <div id="container" style="height: 550px"></div>
    <a-row :gutter="24">
      <a-col :span="6">
        <a-button type="primary" @click="startAnimation">开始回放</a-button>
      </a-col>
      <a-col :span="6">
        <a-button type="primary" @click="pauseAnimation">暂停回放</a-button>
      </a-col>
      <a-col :span="6">
        <a-button type="primary" @click="resumeAnimation">继续回放</a-button>
      </a-col>
      <a-col :span="6">
        <a-button type="primary" @click="stopAnimation">结束回放</a-button>
      </a-col>
    </a-row>
  </div>
</template>

<script>

  import { httpAction, getAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import { validateDuplicateValue } from '@/utils/util'
  import JFormContainer from '@/components/jeecg/JFormContainer'
  import JDate from '@/components/jeecg/JDate'
  import carTPJ from '@assets/img/tpj.png'
  import carYLJ from '@assets/img/ylj.png'

  export default {
    name: 'FrontDeviceRealdataForm',
    components: {
      JFormContainer,
      JDate,
    },
    props: {
      //流程表单data
      formData: {
        type: Object,
        default: ()=>{},
        required: false
      },
      //表单模式：true流程表单 false普通表单
      formBpm: {
        type: Boolean,
        default: false,
        required: false
      },
      //表单禁用
      disabled: {
        type: Boolean,
        default: false,
        required: false
      }
    },
    data () {
      return {
        content:'',
        shebeiNo:'',
        sbtype:'',
        lngdata:[],
        plugins: [],
        position1:[],
        position2:[],
        lineArr:[],
        center: [116.478935, 39.997761],
        datatime_begin:'',
        datatime_end:'',
        form: this.$form.createForm(this),
        model: {},
        labelCol: {
          xs: { span: 24 },
          sm: { span: 5 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 },
        },
        confirmLoading: false,
        validatorRules: {
        },
        url: {
          add: "/tpny/frontDeviceRealdata/add",
          edit: "/tpny/frontDeviceRealdata/edit",
          queryById: "/tpny/frontDeviceRealdata/queryById",
          list: '/clgl/frontDeviceHistorydata/lists',
        }
      }
    },
    computed: {
      formDisabled(){
        if(this.formBpm===true){
          if(this.formData.disabled===false){
            return false
          }
          return true
        }
        return this.disabled
      },
      showFlowSubmitButton(){
        if(this.formBpm===true){
          if(this.formData.disabled===false){
            return true
          }
        }
        return false
      }
    },
    created () {
      //如果是流程中表单，则需要加载流程表单data
      this.showFlowData();
    },
    methods: {
      initMaps(){
        this.map = new AMap.Map("container", {
          resizeEnable: false, //窗口大小调整
          center: [116.478935, 39.997761], //中心 firstArr: [116.478935, 39.997761],
          zoom: 10
        });
      },
//初始化地图
      initMap() {
        this.map = new AMap.Map("container", {
          resizeEnable: true, //窗口大小调整
          center: this.center, //中心 firstArr: [116.478935, 39.997761],
          zoom: 17
        });
        //AMap.plugin('AMap.MoveAnimation', function(){});
        let startIcon1=new AMap.Icon({
          //图标尺寸
          size:new AMap.Size(60,40),
          //图标的取图地址
          image:carTPJ,
          //图标所用图片大小
          imageSize:new AMap.Size(60,40),
          //图标取图偏移量
          //imageOffset:new AMap.Pixel(-9,-3)
        });

        let startIcon2=new AMap.Icon({
          //图标尺寸
          size:new AMap.Size(60,40),
          //图标的取图地址
          image:carYLJ,
          //图标所用图片大小
          imageSize:new AMap.Size(60,40),
          //图标取图偏移量
          //imageOffset:new AMap.Pixel(-9,-3)
        });
        if(this.sbtype=='A'){
          this.marker = new AMap.Marker({
            map: this.map,
            position: this.position1,
            label:{
              content:this.content,
              direction: 'bottom',
            },
            icon: startIcon2,
            offset: new AMap.Pixel(-25, -13),
            autoRotation: true,
             angle:-90,
          });
        }else if(this.sbtype=='B'){
          this.marker = new AMap.Marker({
            map: this.map,
            position: this.position1,
            label:{
              content:this.content,
              direction: 'bottom',
            },
            icon: startIcon1,
            offset: new AMap.Pixel(-25, -13),
            autoRotation: true,
            angle:-90,
          });
        }
      },
      //初始化轨迹
      initroad() {
        var that=this;
        //绘制还未经过的路线
        this.polyline = new AMap.Polyline({
          map: this.map,
          path: this.lineArr,
          showDir: true,
          strokeColor: "#28F", //线颜色--蓝色
          // strokeOpacity: 1,     //线透明度
          strokeWeight: 7 //线宽
          // strokeStyle: "solid"  //线样式
        });
        //绘制路过了的轨迹
        var passedPolyline = new AMap.Polyline({
          map: this.map,
          strokeColor: "#AF5", //线颜色-绿色
          //path: this.lineArr,
          // strokeOpacity: 1,     //线透明度
          strokeWeight: 6 //线宽
          // strokeStyle: "solid"  //线样式
        });
        this.marker.on("moving", e => {
          passedPolyline.setPath(e.passedPath);
          const index=e.passedPath.length
          that.marker.setLabel({
            content:"<div>"+that.lngdata[index].datatime+"-"+that.content+"</div>",
            direction:'top'
          })
        });
        this.map.setFitView(); //合适的视口
      },
      detail(record){//轨迹回放
        //this.form.resetFields()
        //this.model = Object.assign({}, record)
        this.visible = true
        this.sbtype=record.deviceType;
        this.Carguiji(record);
      },
      Query(){//搜索
        if(this.datatime_begin==''){
          this.$message.warning("请选择开始时间")
          return;
        }
        if(this.datatime_end==''){
          this.$message.warning("请选择结束时间")
          return;
        }
        let params = { shebeiNo: this.shebeiNo,datatime_end:this.datatime_end.datatime,datatime_begin:this.datatime_begin}
        getAction(this.url.list, params).then((res) => {
          console.log(res)
          if (res.success) {
            let data = res.result;
            this.lngdata=[];
            this.lngdata=data;
            console.log(this.lngdata,"11111111111111")
            this.lineArr = [];
            this.center=[];
            for (let i = 0; i < data.length; i++) {
              this.lineArr.push([data[i].longitude, data[i].latitude])
              if (i <= data.length - 1) {
                this.center.push(data[i].longitude, data[i].latitude)
              }
            }
            this.position1 =  this.lineArr[0];
            this.initMap();
            this.initroad();
          }else{
            this.initMaps();
            this.$message.error("暂无当前车辆轨迹!")
          }
        })
      },
      Carguiji:function(record){
        let dt = new Date(record.datatime);
        let y = dt.getFullYear();//年
        let m = dt.getMonth() + 1;//月
        let d = dt.getDate();//日
        if(m<10){
          m=0+""+m;
        }
        if(d<10){
          d=0+""+d;
        }
        let datatime_start=y+"-"+m+"-"+d+" "+"00:00:00";
        this.datatime_end=record.datatime;
        this.datatime_begin=datatime_start;
        let params = { shebeiNo: record.shebeiNo,datatime_end:record.datatime,datatime_begin:datatime_start}
        this.content=record.shebeiNo_dictText;
        getAction(this.url.list, params).then((res) => {
          console.log(res)
          if (res.success) {
            let data = res.result;
            this.lngdata=[];
            this.center=[];
            this.lngdata=data;
            this.lineArr = [];
            for (let i = 0; i < data.length; i++) {
              this.lineArr.push([data[i].longitude, data[i].latitude])
              if (i <= data.length - 1) {
                this.center.push(data[i].longitude, data[i].latitude)
              }
            }
            this.position1 =  this.lineArr[0];
            console.log(this.lineArr,"lineArr");
            this.initMap();
            this.initroad();
          }else{
            this.initMaps();
            this.$message.error("暂无当前设备轨迹!")
          }
        })
        console.log(this.center,"center");
      },
      //开始动画
      startAnimation() {
        this.marker.moveAlong(this.lineArr,200);
      },
      //暂停动画
      pauseAnimation() {
        this.marker.pauseMove();
      },
      //继续动画
      resumeAnimation() {
        this.marker.resumeMove();
      },
      //停止动画
      stopAnimation() {
        this.marker.stopMove();
      },
      add () {
        this.edit({});
      },
      edit (record) {
        this.form.resetFields();
        this.model = Object.assign({}, record);
        this.visible = true;
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'deviceType_dictText','shebeiNo_dictText','projectId','speed','temperature','longitude','latitude','datatime'))
        })
      },
      //渲染流程表单数据
      showFlowData(){
        if(this.formBpm === true){
          let params = {id:this.formData.dataId};
          getAction(this.url.queryById,params).then((res)=>{
            if(res.success){
              this.edit (res.result);
            }
          });
        }
      },
      submitForm () {
        const that = this;
        // 触发表单验证
        this.form.validateFields((err, values) => {
          if (!err) {
            that.confirmLoading = true;
            let httpurl = '';
            let method = '';
            if(!this.model.id){
              httpurl+=this.url.add;
              method = 'post';
            }else{
              httpurl+=this.url.edit;
               method = 'put';
            }
            let formData = Object.assign(this.model, values);
            console.log("表单提交数据",formData)
            httpAction(httpurl,formData,method).then((res)=>{
              if(res.success){
                that.$message.success(res.message);
                that.$emit('ok');
              }else{
                that.$message.warning(res.message);
              }
            }).finally(() => {
              that.confirmLoading = false;
            })
          }

        })
      },
      popupCallback(row){
        this.form.setFieldsValue(pick(row,'deviceType_dictText','shebeiNo_dictText','projectId','speed','temperature','longitude','latitude','datatime'))
      },
    }
  }
</script>
<style>
.amap-marker-label{
  background-color:rgba(1,2,73,.0);
  color:#990055;
  border:1px solid rgba(33,150,227,0.50);
}
</style>