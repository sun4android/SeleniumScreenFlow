<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
    <title>FreeMarker</title>
    <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.5/jquery.min.js"></script>
    <script type="text/javascript" src="http://malsup.github.com/chili-1.7.pack.js"></script>
    <script type="text/javascript" src="http://malsup.github.com/jquery.cycle.all.js"></script>
    <script type="text/javascript">
    $(function() {
        $('#s1').cycle('fade');
    });
    </script>
</head>
<body>
    <div id="s1">
    <#list images as image>
        <img src="./images/${image}" width="${width}" height="${height}" />
    </#list>
    </div>

</body>