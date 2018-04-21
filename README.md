# epass-backend

# Config
Database config
```sh
conf/application.yaml
```
System config
```sh
conf/system.propeties
```

# Api Document
1. Account managment

| path     | http-method | params |description|
| -------- | -------- | -------- | -------- |
| /login | get     | <b>username</b>: username</br><b>password</b>: hashmd5 password| return jwt token, use Authentication Bearer|
|/register|post json|<b>username</b>: username </br> <b>password</b>: hashmd5 password</br> <b>email</b>: email (current only gmail)|return user register|
|/reset-password|post json|<b>username</b>: username </br> <b>email</b>: email</br>(current only gmail)|return message check gmail|
|/reset-password-callback|post json|<b>token</b>: token handle from email </br> <b>new_password</b>: new hashmd5 password</br>|config handle token frontend by change resetpassword.broker.uri in system.propeties|
|/v1/change-password|post json|<b>old_password</b>: old hashmd5 password </br> <b>new_password</b>: new hashmd5 password password||

