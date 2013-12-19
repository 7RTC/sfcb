Social Facebook Collage Builder
======

## Demonstração de aplicação web que utiliza HTML5, Google App Engine e RestFB para criar colagens de fotos e publicá-las na conta do usuário no Facebook.

### Requer:
[JDK 7](http://www.oracle.com/technetwork/pt/java/javase/downloads/index.html)  
[Eclipse](http://www.eclipse.org/downloads/)  
[Apache Maven](http://maven.apache.org) 3.1 ou superior  
[App Engine Maven Plugin](http://code.google.com/p/appengine-maven-plugin/)  
[Google Plugin](https://developers.google.com/eclipse/)  

### Setup:
1) Instale o [JDK 7](http://www.oracle.com/technetwork/java/javase/downloads/index.html);

2) Baixe o [Eclipse IDE](http://www.eclipse.org/downloads/) para Java EE ou IDE similar;

3) Instale o [plugin](https://developers.google.com/eclipse/?hl=pt-BR) do Google App Engine (GAE) para Eclipse;

4) *Opcional*: Instale o [SDK do GAE](https://developers.google.com/appengine/downloads?hl=pt-br);

*Caso esteja utilizando o plugin do GAE para o Eclipse, basta instalar o componente "Google App Engine Java SDK" via Atualização de Software do Eclipse. Mais informações na documentação do [Plugin do GAE](https://developers.google.com/eclipse/docs/getting_started?hl=pt-BR);

5) Após a instalação, reinicie o Eclipse;

6) Instale o [Maven 3.1](http://maven.apache.org/) ou superior;

7) *Opcional*: Configure a variável de ambiente do usuário M2_HOME apontando para o diretório do maven e adicioná-la na variável PATH

Para integrar o Maven ao Eclipse utilize o plugin [m2e](http://eclipse.org/m2e/) já presente em versões recentes do IDE:

### Configuração:

1) Na `src/main/resources` copie o arquivo `facebook-app.properties.template` para `facebook-app.properties`.

2) Edite o arquivo `facebook-app.properties` e insira as credenciais do aplicativo criado no Facebook e URL para redirect
do login no Google App Engine conforme instruções do artigo na Java Magazine.

### Intruções de uso:

Para construir o projeto via linha de comando vá para o diretório raiz da aplicação e execute:

    mvn package

Para iniciar a aplicação é necessário executar o comando presente no plugin [App Engine Maven Plugin](http://code.google.com/p/appengine-maven-plugin/):

    mvn appengine:devserver

Para mais informações, consulte a documentação do [Java App Engine](https://developers.google.com/appengine/docs/java/overview).

Para ver todos os goals do App Engine Plugin execute:

    mvn help:describe -Dplugin=appengine