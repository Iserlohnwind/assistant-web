mvn clean -U package -Dmaven.test.skip=true
scp target/assistant-web.jar root@118.25.43.216:/data/workspace/
ssh root@118.25.43.216 "nohup bash /data/workspace/run.sh"