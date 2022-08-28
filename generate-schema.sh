mvn datanucleus:schema-create \
  -DpersistenceUnitName=MyTest \
  -Dprops=datanucleus.properties \
  -DcompleteDdl=true \
  -DddlFile=schema.sql \
  -Dlog4jConfiguration=log4j.properties