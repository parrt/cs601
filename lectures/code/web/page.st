page(users) ::= <<
<! a comment !>
<header(title="foo")>
<users:{u| <u.name> <u.parkingSpot>}; separator="\n">
<users:showuser(); separator=", ">
>>
