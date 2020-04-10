FROM openjdk:7
ARG ADMIN_PASS=admin

ENV GLASSFISH_BASE /usr/local/glassfish
ENV GLASSFISH_HOME $GLASSFISH_BASE/glassfish3
ENV DOMAIN_HOME $GLASSFISH_HOME/glassfish/domains/domain1

RUN mkdir $GLASSFISH_BASE
WORKDIR $GLASSFISH_BASE

COPY  ogs-3.1.2.2-web.zip ./
#RUN wget https://edelivery.oracle.com/otn-pub/java/glassfish/3122/ogs-3.1.2.2-web.zip\
        ##&&
RUN    unzip ogs-3.1.2.2-web.zip\
        && rm ogs-3.1.2.2-web.zip
RUN wget https://jdbc.postgresql.org/download/postgresql-9.3-1104.jdbc41.jar\
        && mv postgresql-9.3-1104.jdbc41.jar $DOMAIN_HOME/lib/

WORKDIR $GLASSFISH_HOME
COPY domain.xml $DOMAIN_HOME/config
COPY target/deploy-1.0.war /build/



ENV ASADMIN_CMD $GLASSFISH_HOME/bin/asadmin
ENV PWD_FILE $GLASSFISH_BASE/pwd
ENV PWD_CHANGE_FILE $GLASSFISH_BASE/changepwd
ENV PWD_ALIAS_FILE $GLASSFISH_BASE/alias
RUN  cd $GLASSFISH_HOME/glassfish && touch /tmp/password.txt && \
    chmod 600 /tmp/password.txt && echo "AS_ADMIN_PASSWORD=" > /tmp/password.txt && \
    echo "AS_ADMIN_NEWPASSWORD=$ADMIN_PASS" >> /tmp/password.txt && \
    $ASADMIN_CMD --user admin --passwordfile /tmp/password.txt change-admin-password --domain_name domain1 && \
    rm /tmp/password.txt

WORKDIR $GLASSFISH_BASE
#RUN $ASADMIN_CMD start-domain domain1 && \
#    echo 'AS_ADMIN_PASSWORD=' > $PWD_CHANGE_FILE && \
#    echo "AS_ADMIN_NEWPASSWORD=$ADMIN_PASS" >> $PWD_CHANGE_FILE && \
#    echo "AS_ADMIN_PASSWORD=$ADMIN_PASS" >> $PWD_FILE && \
#    echo "AS_ADMIN_PASSWORD=" >> $PWD_ALIAS_FILE && \
#    echo "AS_ADMIN_ALIASPASSWORD=$ADMIN_PASS" >> $PWD_ALIAS_FILE && \
#    #$ASADMIN_CMD --interactive=false --user admin --passwordfile $PWD_ALIAS_FILE create-password-alias ad && \
#    #$ASADMIN_CMD --interactive=false enable-secure-admin-internal-user --passwordAlias ad admin && \
#    #$ASADMIN_CMD --interactive=false --user admin --passwordfile $PWD_CHANGE_FILE change-admin-password --domain_name domain1 && \
#    #$ASADMIN_CMD --interactive=false enable-secure-admin && \
#    $ASADMIN_CMD stop-domain

COPY start.sh .
RUN chmod a+x start.sh

EXPOSE 4848 8080 8181
CMD ./start.sh
