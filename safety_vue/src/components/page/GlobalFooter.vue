<template>
    <div class="footer">
        <div class="right">
            <!--组织机构-->
            <a-popover trigger="hover">
                <template slot="content">
                    <p>组织机构</p>
                </template>
                <div class="showd" @click="showDrawer">
                  组织机构
                </div>
                <a-drawer
                    width="280"
                    title="组织机构"
                    placement="right"
                    :closable="true"
                    :visible="visible"
                    :after-visible-change="afterVisibleChange"
                    :mask="true"
                    @close="onClose"
                >
                    <Sysdepart v-model="currentOrgCode" v-on:changeVisible="changeVisible" />
                </a-drawer>
            </a-popover>
        </div>
        <div class="rights">
            <!--分部分项-->
            <a-popover trigger="hover">
                <template slot="content">
                    <p>分部分项工程</p>
                </template>
                <div class="showd" @click="showDrawer1">
                  分部分项
                </div>
                <a-drawer
                    width="480"
                    title="分部分项工程"
                    placement="right"
                    :closable="true"
                    :visible="visible1"
                    :after-visible-change="afterVisibleChange1"
                    :mask="true"
                    @close="onClose1"
                >
                    <Safetypartialbasic v-model="currentOrgCode" v-on:changeVisible1="changeVisible1" />
                </a-drawer>
            </a-popover>
        </div>
        <!--    <div class="links">-->
        <!--      <a href="http://www.jeecg.com" target="_blank">JEECG 首页</a>-->
        <!--      <a href="https://github.com/zhangdaiscott/jeecg-boot" target="_blank">-->
        <!--        <a-icon type="github"/>-->
        <!--      </a>-->
        <!--      <a href="https://ant.design/">Ant Design</a>-->
        <!--      <a href="https://vuecomponent.github.io/ant-design-vue/docs/vue/introduce-cn/">Vue Antd</a>-->
        <!--    </div>-->
        <!--    <div class="copyright">-->
        <!--      Copyright-->
        <!--      <a-icon type="copyright"/>-->
        <!--      2021 <span>浙江高速信息工程技术有限公司 400-808-6362</span>-->
        <!--    </div>-->
    </div>
</template>

<script>
import Sysdepart from '@comp/page/Sysdepart'
import Safetypartialbasic from '@comp/page/Safetypartialbasic'
export default {
    name: 'LayoutFooter',
    components: {
        Sysdepart,
        Safetypartialbasic,
    },
    data() {
        return {
            visible: false,
            visible1: false,
            currentOrgCode: '',
        }
    },
    methods: {
        afterVisibleChange(val) {
            console.log(val)
            console.log('visible', val)
        },
        afterVisibleChange1(val) {
            console.log('visible', val)
            console.log(val)
        },
        changeVisible(value) {
            //sysdepart 为子组件 传值修改关闭抽屉的参数
            console.log(value)
            this.visible = value
        },
        changeVisible1(value) {
            //safetypartialbasic 为子组件 传值修改关闭抽屉的参数
            console.log(value)
            this.visible1 = value
        },
        showDrawer() {
            this.visible = true
        },
        onClose() {
            this.visible = false
        },
        showDrawer1() {
            this.visible1 = true
        },
        onClose1() {
            this.visible1 = false
        },
    },
}
</script>

<style lang="less" scoped>
.footer {
    //position: relative;
    padding: 0 16px;
    margin: 48px 0 24px;
    text-align: center;

    .links {
        margin-bottom: 8px;

        a {
            color: rgba(0, 0, 0, 0.45);

            &:hover {
                color: rgba(0, 0, 0, 0.65);
            }

            &:not(:last-child) {
                margin-right: 40px;
            }
        }
    }
    .copyright {
        color: rgba(0, 0, 0, 0.45);
        font-size: 14px;
    }
}
.right {
    position: fixed;
    right: 0;
    bottom: 60%;
    z-index: 80;
}
.rights {
    position: fixed;
    right: 0;
    bottom: 30%;
    z-index: 80;
}
.showd{
    width: 32px;
    color: #fff;
    background-color: #0C264D;
    border-color: #0C264D;
    cursor: pointer;
    writing-mode: vertical-lr;
    touch-action: manipulation;
    line-height: 32px;
    padding: 15px 0;
    font-size: 14px;
    border-radius: 4px;
    letter-spacing: 5px;
}
</style>