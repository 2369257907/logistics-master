<template>
  <div>
    <a-table :loading="loading" :columns="columns" :data-source="data" rowKey="id">
      <a slot="company" slot-scope="company">{{ company }}</a>
      <span slot="customTitle"><a-icon type="bank" /> 公司名称</span>
      <span slot="action" slot-scope="text, record, index">
        <a-tag color="red" v-if="!record.pay">等待结款</a-tag>
        <a-tag color="green" v-if="record.pay">结款完成</a-tag>
        <a-button v-if="!record.pay" type="link" @click="confirm(record, index)">结款</a-button>
      </span>
    </a-table>

  </div>
</template>

<script>
import {FindAllSale, SaveSale} from "../../api/sale";
import {InAndOut} from "../../api/inventory";
import {FindAllCommodity} from "@/api/commodity";
import {FindAllWarehouse} from "@/api/warehouse";


import Commodity from "@/views/basics/Commodity";

const columns = [
  {
    dataIndex: 'company',
    key: 'company',
    slots: {title: 'customTitle'},
    scopedSlots: {customRender: 'company'},
  },
  {
    title: '打款帐号',
    dataIndex: 'number',
    key: 'number',
  },
  {
    title: '商品',
    dataIndex: 'commodity',
    key: 'commodity',
  },
  {
    title: '出货仓库',
    dataIndex: 'warehouse',
    key: 'warehouse',
  },
  {
    title: '数量',
    dataIndex: 'count',
    key: 'count',
  },
  {
    title: '总计',
    dataIndex: 'price',
    key: 'price',
  },
  {
    title: '预留电话',
    key: 'phone',
    dataIndex: 'phone',
  },
  {
    title: '备注',
    dataIndex: 'description',
    key: 'description',
  },
  {
    title: '开票时间',
    dataIndex: 'createAt',
    key: 'createAt',
  },
  {
    title: '更多操作',
    key: 'action',
    scopedSlots: {customRender: 'action'},
  },
];

const data = [];

export default {

  data() {
    return {
      loading: false,
      data: [],
      columns,
      form: {
        wid: '',
        cid: '',
        name: '',
        description: '',
        count: '',
      },
      CommodityList: [],
      warehouseList: [],
    };
  },

  mounted() {
    //获取商品列表
    FindAllCommodity().then((res) => {
      setTimeout(() => {
        this.loading = false
        this.CommodityList = res.data
        console.log(this.CommodityList)
      }, 600)
    })
    //获取仓库列表
    FindAllWarehouse().then((res) => {
      console.log(res.data)
      if (res.status) this.warehouseList = res.data
    })

    this.loadTableData()
  },

  methods: {

    loadTableData() {
      this.loading = true
      FindAllSale().then((res) => {
        setTimeout(() => {
          this.loading = false
          this.data = res.data
        }, 600)
      })
    },

    confirm(record, index) {

      let that = this
      this.$confirm({
        title: '销售结款',
        content: '我已确定' + record.company + '的销售金额 ¥' + record.price + '已经打入账户!',
        okText: '确认',
        cancelText: '取消',
        onOk() {
          //获取商品描述
          that.form.description = record.description;
          // alert(record.description)
          //获取商品名、商品数量
          that.form.name = record.commodity;
          that.form.count = record.count;
          //获取商品cid
          for (var i=0;i<that.CommodityList.length;i++){
            if (that.CommodityList[i].name === record.commodity){
              // alert(that.CommodityList[i].name)
              that.form.name = record.commodity;
              that.form.cid = that.CommodityList[i].id;
              break;
            }
          }
          //获取仓库wid
          for (var i=0;i<that.warehouseList.length;i++){
            if (that.warehouseList[i].name === record.warehouse){
              that.form.wid = that.warehouseList[i].id
            }
          }

          console.log(that.form)

          //出库商品，减库存
          InAndOut('out', that.form).then((res) => {
            if (res.status){
              that.$message.success("操作成功")
              that.data[index].pay = true
              SaveSale(that.data[index]).then((res) => {
                if (res.status) that.$message.success("销售结款成功")
              })
            }

            console.log(res)
          })

        },
      });
    },


  },

};
</script>

<style scoped>
.editable-add-btn {
  margin-bottom: 15px;
}
</style>
