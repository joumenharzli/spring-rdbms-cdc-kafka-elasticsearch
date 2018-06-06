# 
#  Copyright (C) 2018 Joumen Harzli
# 
#  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
#  in compliance with the License. You may obtain a copy of the License at
# 
#  http://www.apache.org/licenses/LICENSE-2.0
# 
#  Unless required by applicable law or agreed to in writing, software distributed under the License
#  is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
#  or implied. See the License for the specific language governing permissions and limitations under
#  the License.
# 

FROM openjdk:8u171-jre

MAINTAINER Joumen HARZLI

ENV SPRING_OUTPUT_ANSI_ENABLED=ALWAYS

ADD target/user-denormalizer.jar ./app.jar

EXPOSE 8084

CMD java ${JAVA_ARGS} -XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap \
                      -XX:+UseG1GC -Djava.security.egd=file:/dev/./urandom \
                      -jar /app.jar