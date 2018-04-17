# sell
springboot实战入门，springboot+jpa实现java后台api接口

#实现功能
- 1 购物车功能
- 2 java后台api接口
- 3 简单的电商订单后台
- 4 javaweb网站
- 5 微信小游戏体验 

#### 体验地址：https://30paotui.com/

#### 项目中的sql.txt文档中是创建对应表格所需的sql语句

##api接口说明文档
####一，获取pv访问量
url如下，采用get请求
https://30paotui.com/pv/2048/list 
返回的json数据如下
 ```
 {
    "code": 100,
    "msg": "成功",
    "data": 342
}
```
#返回数据格式如下
```
{
    "code": 100,
    "msg": "成功",
    "data": [
        {
            "name": "热销",
            "type": 1,
            "foods": [
                {
                    "id": "5",
                    "name": "奶茶",
                    "price": 8,
                    "desc": "奶茶妹妹嫁给了刘强东",
                    "icon": null,
                    "createTime": 1520823301000
                }
            ]
        },
        {
            "name": "包子类",
            "type": 3,
            "foods": [
                {
                    "id": "3",
                    "name": "包子",
                    "price": 4,
                    "desc": "包子好吃",
                    "icon": null,
                    "createTime": 1513481102000
                },
                {
                    "id": "4",
                    "name": "蟹黄包",
                    "price": 6,
                    "desc": "蟹黄包比较贵",
                    "icon": null,
                    "createTime": 1513481642000
                }
            ]
        }       
    ]
}
```
