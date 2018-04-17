#移动打包好以后的文件并改名
#这里的-f参数判断$myFile是否存在
myFile="target/sell-1.0.jar"
if [ ! -f "$myFile" ]
then
   mv "$myFile" qcl80.jar
　　echo  move "$myFile"
fi
