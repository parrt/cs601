page(users,phones) ::= <<
<! <header(title="foo")> !>

parrt's phone is <phones.parrt>

<users:{u | <u.name> has ID <u.id> manager: <u.manager>}; separator="\n">


<phones,users:{p,u |
    <u.name>: <phones.(p)> <! p is set of keys in phones !>
}>

<users:{u | <phones.(u.name)>}>

<if(!users)>heh we have users<else>laksdjfklasjf<endif>

<users:showuser(); separator=", ">
>>
