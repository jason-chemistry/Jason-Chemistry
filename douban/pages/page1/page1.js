
//拿到应用程序对象
const app=getApp()

Page({
     //data类中含有数据
    data:{
        message:'hello word',
        list:[]//一个默认为空的数组
    },
    onLoad(){
        //通过
        const msg=app.foo()
        console.log(msg)
       // this.setData({
        //    message:Date.now()
       // })

         //微信自身的api接口
    const _this=this;   
     //获取豆瓣的api :正在热映的电影 https://api.douban.com/v2/movie/in_theaters
     wx.request({
    url: 'https://api.douban.com/v2/movie/in_theaters', //仅为示例，并非真实的接口地址
    data: {},//发送给服务端的参数
     header: {
      'content-type': 'application/json' // 默认值
     },
      //请求完成后利用success来回调
      success: function(res) {
      
        console.log(res.data)
         _this.setData({
         list:res.data.subjects
        })
      }
   })
}
  
})
