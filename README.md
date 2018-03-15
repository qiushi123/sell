# sell
springboot实战入门，springboot+jpa实现java后台api接口

#实现功能
- 1 购物车功能
- 2 java后台api接口
- 3 简单的电商订单后台

#### 项目中的sql.txt文档中是创建对应表格所需的sql语句
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
