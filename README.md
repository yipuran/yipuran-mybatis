# yipuran-mybatis
mybatis used application

## Dependency
mybatis

## Document
Extract doc/yipuran-mybatis-doc.zip and see the Javadoc
or [Wiki Page](../../wiki)

## Setup pom.xml
```
<repositories>
   <repository>
      <id>yipuran-mybatis</id>
      <url>https://raw.github.com/yipuran/yipuran-mybatis/mvn-repo</url>
   </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>org.yipuran.mybatis</groupId>
        <artifactId>yipuran-mybatis</artifactId>
        <version>4.13</version>
    </dependency>
</dependencies>
```


## Setup gradle
```
repositories {
    mavenCentral()
    maven { url 'https://raw.github.com/yipuran/yipuran-mybatis/mvn-repo'  }
}

dependencied {
    compile 'org.yipuran.mybatis:yipuran-mybatis:4.13'
}
```
