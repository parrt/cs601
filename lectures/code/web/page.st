page(users,phones) ::= <<
<! <header(title="foo")> !>

parrt's phone is <phones.("parrt")>

<phones,users:{p,u |
    <u.name>: <phones.(p)> <! p is set of keys in phones !>
}>

<users:{u | <phones.(u.name)>}>

<users:showuser(); separator=", ">
>>
