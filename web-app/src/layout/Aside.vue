<template>
  <a-layout-sider
      :style="{ overflow: 'auto', height: '100vh', position: 'fixed', left: 0 }"
      width="220">
    <div class="logo">
      <img src="../assets/logo.svg" alt="">
      物流管理系统
    </div>
    <a-menu theme="dark" mode="inline">
      <a-sub-menu v-for="(item, index) in menus" :key="index" v-show=item.isShow>
        <span slot="title" >
          <a-icon :type="item.icon"/>
          <span>{{ item.title }}</span>
        </span>
        <a-menu-item v-for="menu in item.children" :key="menu.title">
          <router-link :to="menu.path">
            {{ menu.title }}
          </router-link>
        </a-menu-item>
      </a-sub-menu>
    </a-menu>
  </a-layout-sider>
</template>

<script>


import {SaveDistribution} from "@/api/distribution";

export default {

  data() {
    return {
      show: false,
      menus: [
        {
          title: '基础信息管理',
          icon: 'home',
          children: [
            {title: '来往单位', path: '/company'},
            {title: '员工管理', path: '/employee'},
          ],
          showRoles:["ROLE_EMPLOYEE","ROLE_SUPER_ADMIN"],
          isShow:false,
        },
        {
          title: '商品管理',
          icon: 'credit-card',
          children: [
            {title: '商品管理', path: '/commodity'},
          ],
          showRoles:["ROLE_COMMODITY","ROLE_SUPER_ADMIN"],
          isShow:false,

        },
        {
          title: '仓库管理',
          icon: 'apartment',
          children: [
            {title: '仓库管理', path: '/warehouse'},
          ],
          showRoles:["ROLE_WAREHOUSE","ROLE_SUPER_ADMIN"],
          isShow:false,
        },
        {
          title: '销售信息管理',
          icon: 'pay-circle',
          children: [
            {title: '销售开票', path: '/sale/create'},
            {title: '销售记录', path: '/sale/record'},
          ],
          showRoles:["ROLE_SALE","ROLE_SUPER_ADMIN"],
          isShow:false,

        },
        {
          title: '配送信息管理',
          icon: 'car',
          children: [
            {title: '申请配送', path: '/delivery/create'},
            {title: '配送列表', path: '/delivery/list'},
          ],
          showRoles:["ROLE_SUPER_ADMIN"],
          isShow:false,
        },
        {
          title: '运输信息管理',
          icon: 'rocket',
          children: [
            {title: '车辆资料', path: '/vehicle'},
            {title: '驾驶员资料', path: '/driver'},
          ],
          showRoles:["ROLE_SUPER_ADMIN"],
          isShow:false,

        },
        {
          title: '系统决策管理',
          icon: 'line-chart',
          children: [
            {title: '入库分析', path: '/analyze/in'},
            {title: '出库分析', path: '/analyze/out'},
          ],
          showRoles:["ROLE_SUPER_ADMIN"],
          isShow:false,

        },
        {
          title: '系统信息管理',
          icon: 'tool',
          children: [
            {title: '安全设置', path: '/security'},
            {title: '操作员管理', path: '/admin'},
            {title: '权限列表', path: '/role'},
          ],
          showRoles:["ROLE_SUPER_ADMIN"],
          isShow:false,
        },
      ]
    }
  },
  mounted() {
      if (this.$store.state.user.details.roles ==="ROLE_COMMODITY"){
        this.show = true;
      }
      this.checkIsShow()
  },
  methods: {
    checkIsShow(){
      console.log(this.menus[0].showRoles)
      //获取身份信息roles
      var roles = this.$store.state.user.details.roles;
      var rolesArr = roles.split(";");
      //判断roles是否包含需要的身份
      for (var j=0;j<this.menus.length;j++){
        for (var i=0;i<rolesArr.length;i++){
          console.log(this.menus[0])
          if(this.menus[j].showRoles.includes(rolesArr[i])){
            this.menus[j].isShow = true
          }
        }
      }
    }
  },

}

</script>

<style scoped>

#components-layout-demo-fixed-sider .logo {
  padding: 10px 15px;
  height: 50px;
  font-size: 15px;
  margin: 16px;
  color: #ffffff;
  letter-spacing: 2px;
}

.ant-menu {
  letter-spacing: 1px;
}

.logo img {
  width: 32px;
  height: 32px;
  margin-right: 5px;
}

</style>