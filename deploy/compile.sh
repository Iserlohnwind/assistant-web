mvn clean -U package -Dmaven.test.skip=true
scp target/assistant-web.jar root@cloud_machine:/data/workspace/