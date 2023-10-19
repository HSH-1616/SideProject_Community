pipeline {
    agent any
     environment {
        SPRING_CONFIG_IMPORT = "${SPRING_CLOUD_SERVER}"
    }
    stages {
        stage('App Test') {
            steps {
                sh "chmod +x gradlew"
                sh "./gradlew clean test --parallel"

                echo "App Test Success"
            }
        }

        stage('App Build') {
            steps {
                sh "chmod +x gradlew"
                sh "./gradlew clean bootJar"

                echo "App Build Success"
            }
        }

        stage('Docker Login') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: 'docker_hub', usernameVariable: 'DOCKER_CREDS_USR', passwordVariable: 'DOCKER_CREDS_PSW')]) {
                        sh "echo \$DOCKER_CREDS_PSW | docker login -u \$DOCKER_CREDS_USR --password-stdin"

                        echo "Docker Login Success"
                    }
                }
            }
        }

        stage('Docker Build Dev') {
            when{
                //dev 브랜치 Push
                expression { env.GIT_BRANCH == 'dev' }
            }
            steps {
                script {
                    //Build시 사용된 캐시 제거
                    sh "yes | docker builder prune"

                    // 이미지 Build
                    sh "docker build -t ${DOCKER_ID}/${DOCKER_REPO_COMMUNITY}:dev ."

                    // Docker Hub에 Push
                    sh "docker push ${DOCKER_ID}/${DOCKER_REPO_COMMUNITY}:dev"

                    // Push한 이미지 삭제
                    sh "docker rmi ${DOCKER_ID}/${DOCKER_REPO_COMMUNITY}:dev"

                    echo "Image Build Success"
                }
            }
        }

        stage('Docker Build Prod') {
            when{
                //main 브랜치 Push
                expression { env.GIT_BRANCH == 'main' }
            }
            steps {
                script {
                    //docker image의 첫 tag를 확인 후, 다음 버전의 image를 생성
                    def currentTag = sh(script: "docker images | awk -v DOCKER_REPOSITORY_NAME=${DOCKER_REPO_COMMUNITY} '{if (\$1 == DOCKER_REPOSITORY_NAME) print \$2;}'", returnStdout: true)

                    // [0-9]\.[0-9]으로 버전으로 관리
                    def newTag
                    if (currentTag =~ /^[0-9]+\.[0-9]+$/) {
                        newTag = (currentTag as Double) + 0.1
                        newTag = String.format('%.1f', newTag as Double)
                        echo "현재 Version $currentTag"
                        echo "새로운 Version $newTag"
                    } else {
                        //새로운 이미지일 경우 0.1
                        echo "이미지 신규 생성"
                        newTag= '0.1'
                    }

                    // 기존 이미지 삭제
                    if (newTag != '0.1') {
                        sh "docker rmi ${DOCKER_REPO_COMMUNITY}:$currentTag"
                    }

                    //build시 사용된 캐시 제거
                    sh "docker builder prune"

                    // 이미지 Build
                    sh "docker build -t ${DOCKER_REPO_COMMUNITY}:$newTag ."
                    sh "docker tag ${DOCKER_REPO_COMMUNITY}:$newTag ${DOCKER_ID}/${DOCKER_REPO_COMMUNITY}:$newTag"
                    sh "docker tag ${DOCKER_REPO_COMMUNITY}:$newTag ${DOCKER_ID}/${DOCKER_REPO_COMMUNITY}:latest"

                    // Docker Hub에 Push
                    sh "docker push ${DOCKER_ID}/${DOCKER_REPO_COMMUNITY}:$newTag"
                    sh "docker push ${DOCKER_ID}/${DOCKER_REPO_COMMUNITY}:latest"

                    // Push한 이미지 삭제
                    sh "docker rmi ${DOCKER_ID}/${DOCKER_REPO_COMMUNITY}:$newTag"
                    sh "docker rmi ${DOCKER_ID}/${DOCKER_REPO_COMMUNITY}:latest"

                    echo "Image Build Success"
                }
            }
        }

        stage('Deploy Dev') {
            when {
                //dev 브랜치 Push
                expression { env.GIT_BRANCH == 'dev' }
            }
            steps {
               sshagent (credentials: ['dev']){
                    // 이미 실행 중인 Docker 컨테이너가 있으면 중지하고 삭제
                    sh " ssh -p ${DEV_PORT} -o StrictHostKeyChecking=no ${DEV_USER}@${DEV_ADDRESS} '/usr/local/bin/docker stop ${DOCKER_REPO_COMMUNITY}_dev && /usr/local/bin/docker rm ${DOCKER_REPO_COMMUNITY}_dev && /usr/local/bin/docker rmi ${DOCKER_ID}/${DOCKER_REPO_COMMUNITY}:dev || true'"
                    // Docker Hub에서 Pull받고 실행
                    sh " ssh -p ${DEV_PORT} -o StrictHostKeyChecking=no ${DEV_USER}@${DEV_ADDRESS} '/usr/local/bin/docker pull ${DOCKER_ID}/${DOCKER_REPO_COMMUNITY}:dev'"
                    sh " ssh -p ${DEV_PORT} -o StrictHostKeyChecking=no ${DEV_USER}@${DEV_ADDRESS} '/usr/local/bin/docker run -d -p 8000:8080 --name ${DOCKER_REPO_COMMUNITY}_dev -e SPRING_CONFIG_IMPORT=${SPRING_CLOUD_SERVER} -e SPRING_PROFILES_ACTIVE=dev ${DOCKER_ID}/${DOCKER_REPO_COMMUNITY}:dev'"

                    echo "Deploy Success"
               }
            }
        }

        stage('Deploy Prod'){
            when{
                //main 브랜치 Push
                expression { env.GIT_BRANCH == 'main' }
            }
            steps{
                sshagent (credentials: ['aws']){
                    // 이미 실행 중인 Docker 컨테이너가 있으면 중지하고 삭제
                    sh "ssh -p ${AWS_PORT} -o StrictHostKeyChecking=no ${AWS_USER}@${AWS_ADDRESS} 'docker stop ${DOCKER_REPO_COMMUNITY}_prod && docker rm ${DOCKER_REPO_COMMUNITY}_prod && docker rmi ${DOCKER_ID}/${DOCKER_REPO_COMMUNITY}:latest || true'"
                    // Docker Hub에서 Pull받고 실행
                    sh "ssh -p ${AWS_PORT} -o StrictHostKeyChecking=no ${AWS_USER}@${AWS_ADDRESS} 'docker pull ${DOCKER_ID}/${DOCKER_REPO_COMMUNITY}:latest'"
                    sh "ssh -p ${AWS_PORT} -o StrictHostKeyChecking=no ${AWS_USER}@${AWS_ADDRESS} 'docker run -d -p 80:8080 --name ${DOCKER_REPO_COMMUNITY}_prod -e SPRING_CONFIG_IMPORT=${SPRING_CLOUD_SERVER} -e SPRING_PROFILES_ACTIVE=prod ${DOCKER_ID}/${DOCKER_REPO_COMMUNITY}:latest'"

                    echo "Deploy Success"
                }
            }
        }
    }
}