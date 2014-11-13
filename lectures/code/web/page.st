page(users,phones) ::= <<
<! <header(title="foo")> !>

parrt's phone is <phones.("parrt")>

<phones,users:{p |
    <p>: <phones.(p)>
}>

<users:{u | <phones.(u.name)>}>

<users:showuser(); separator=", ">
>>
