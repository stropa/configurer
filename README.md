# configurer

This is a small helper service that provides UI for changing properties of objects in runtime. 
For apps written in Java + Spring.

Case:
You have a POJO bean where you store part of your configuration properties for some service 
(for example instance of MongoProperties class, annotated with @ConfigurationProperties),
give it to `Configurer` and you will bet generated HTML form in embedded web interface.
Form submit will update changed properties in backend. For this Configurer will add dynamic `PropertySource` to
Spring `MutablePropertySource`.
