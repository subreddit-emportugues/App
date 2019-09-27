# Aplicativo

## Descrição
Aplicativo para Android que exibe informações sobre subreddits lusófonos e afins publicados no subreddit [r/EmPortugues](https://www.reddit.com/r/EmPortugues/).

O aplicativo usa [Retrofit](https://square.github.io/retrofit/) para descarregar um arquivo em formato JSON armazenado num servidor remoto a fim de desserializá-lo num POJO convertido por meio de [GSON](https://github.com/google/gson) e apresentar os resultados numa `ListView` com aparência de tabela.

A atividade principal do aplicativo permite filtrar a base de dados com `SearchView`, reordenar as colunas da tabela com `Comparator` e conferir e visitar itens da lista com `Theme.AppCompat.Dialog` e `Intent` além de contar com um `FloatingActionButton` para facilitar a navegação e um `Menu` com links para o [subreddit](https://www.reddit.com/r/EmPortugues/), o [site](https://emportugues.org/) e este repositório.

As informações apresentadas no aplicativo são: `"icon"`, `"name"`, `"description"`, `"recent_submissions"` e `"recent_comments"`, `"members"`, `"age"`, `"moderators"`, `"nsfw"` e o link de cada subreddit listado.

## Sumário
* [Instalação](#Instalação)
* [Instruções](#Instruções)
* [Dependências](#Dependências)
* [Colaboração](#Colaboração)
* [Demonstração](#Demonstração)
* [Referências](#Referências)

## Instalação
1. Baixe o repositório;
2. descomprima o arquivo;
3. execute o [Android Studio](https://developer.android.com/studio/);
4. abra um novo projeto;
5. e selecione "App-master".

## Instruções
Para alterar o endereço do arquivo JSON, em [RetroClient.java](https://github.com/subreddit-emportugues/aplicativo/blob/master/app/src/main/java/org/emportugues/aplicativo/retrofit/api/RetroClient.java) e em [ApiService.java](https://github.com/subreddit-emportugues/aplicativo/blob/master/app/src/main/java/org/emportugues/aplicativo/retrofit/api/ApiService.java), edite:
```
private static final String ROOT_URL = "https://emportugues.org";
```
```
@GET("/data/subreddits.json")
```

Para alterar as chaves do objeto JSON, em [Subreddit.java](https://github.com/subreddit-emportugues/aplicativo/blob/master/app/src/main/java/org/emportugues/aplicativo/model/Subreddit.java), edite:
```
@SerializedName("id")
@Expose
private Integer id;
@SerializedName("icon")
@Expose
private String icon;
@SerializedName("name")
@Expose
private String name;
@SerializedName("description")
@Expose
private String description;
@SerializedName("recent_submissions")
@Expose
private int submissions;
@SerializedName("recent_comments")
@Expose
private int comments;
@SerializedName("total_activity")
@Expose
private double total_activity;
@SerializedName("members")
@Expose
private Number members;
@SerializedName("age")
@Expose
private Long age;
@SerializedName("moderators")
@Expose
private ArrayList<String> moderators;
@SerializedName("nsfw")
@Expose
private Boolean nsfw;
```

Para alterar o endereço da `WebView`, em [strings.xml](https://github.com/subreddit-emportugues/aplicativo/blob/master/app/src/main/res/values/strings.xml), edite:
```
<string name="url_website">https://emportugues.org</string>
```

## Dependências
> SDK
```
minSdkVersion 19
targetSdkVersion 29
```
> Gradle
```
distributionUrl=https\://services.gradle.org/distributions/gradle-5.4.1-all.zip
```
```
classpath 'com.android.tools.build:gradle:3.5.0'
```
> Permissions
```
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
```
> Java 8
```
sourceCompatibility JavaVersion.VERSION_1_8
targetCompatibility JavaVersion.VERSION_1_8
```
> Android X
```
android.useAndroidX=true
android.enableJetifier=true`
```
> Jetpack
```
implementation 'androidx.appcompat:appcompat:1.0.2'
implementation 'androidx.exifinterface:exifinterface:1.0.0'
implementation 'com.google.android.material:material:1.1.0-alpha09'
annotationProcessor 'androidx.annotation:annotation:1.1.0'
```
> Retrofit
```
implementation 'com.squareup.retrofit2:retrofit:2.3.0'
implementation 'com.squareup.retrofit2:converter-gson:2.3.0'
```
> GSON
```
implementation 'com.google.code.gson:gson:2.8.5'
```
> SDP/SSP
```
implementation 'com.intuit.sdp:sdp-android:1.0.6'
implementation 'com.intuit.ssp:ssp-android:1.0.6'
```
> Glide
```
implementation 'com.github.bumptech.glide:glide:4.9.0'
annotationProcessor 'com.github.bumptech.glide:compiler:4.9.0'
```
> ExpandableTextView
```
implementation 'com.ms-square:expandableTextView:0.1.4'
```
> Butter Knife
```
implementation 'com.jakewharton:butterknife:10.1.0'
annotationProcessor 'com.jakewharton:butterknife-compiler:10.1.0'
```

## Colaboração

Você pode colaborar com o desenvolvimento deste repositório!

[Confira os kanbans deste projeto](https://github.com/orgs/subreddit-emportugues/projects/5), [entre em contato com a equipe de moderação](https://reddit.com/message/compose?to=/r/EmPortugues) e [participe da equipe de desenvolvimento](https://github.com/orgs/subreddit-emportugues/teams/desenvolvedores) para saber a respeito do progresso deste repositório caso queira colaborar antes de [reportar um novo problema](https://github.com/subreddit-emportugues/aplicativo/issues) ou [solicitar o recebimento de uma modificação](https://github.com/subreddit-emportugues/aplicativo/pulls).

## Demonstração

[Baixe o aplicativo para entender como funciona.](https://play.google.com/store/apps/details?id=org.emportugues.aplicativo) ![](/aplicativo.gif)

## Referências

* Aplicativo: https://play.google.com/store/apps/details?id=org.emportugues.aplicativo
* Comunidade: https://www.reddit.com/r/EmPortugues
* Organização: https://github.com/subreddit-emportugues
* Repositório: https://github.com/subreddit-emportugues/App
* Projeto: https://github.com/orgs/subreddit-emportugues/projects/5
* Equipe: https://github.com/orgs/subreddit-emportugues/teams/desenvolvedores
* Licença: 
