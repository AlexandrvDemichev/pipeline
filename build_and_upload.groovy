//package main
def tmp_group_id = 'Nexus_PROD'
pipeline {
    agent any
    parameters {
        string(defaultValue: '', description: 'Версия дистрибутива', name: 'version')
        string(defaultValue: 'ID000000', description: 'ID системы', name: 'ID_system')
        
    }
    options{

        timestamps()
    }
    stages {
        stage('Get upload file') {
            steps{
                script{
                    echo 'Get upload file'
                    sh 'pwd'
                    sh 'ls -l'
                    echo 'Upload file text: '
                    sh 'cat file.txt'
  
                        
                }
            }
        }
        
        stage('Build archive') {
            steps{
                script{
                    echo 'Build archive'
                    try{
                        sh 'zip file.zip file.txt'
                    }catch(Exception ee){
                            echo 'EXCEPTION: ' + ee.getMessage()
                            currentBuild.result='ABORTED'
                            return
                    }
                    
                    
                }
            }
        }
        stage('Upload file to nexus') {
            steps{
                script{
                    echo 'Upload file to nexus'
                    try{
                        withCredentials([usernamePassword(credentialsId: 'nexus',
                                      passwordVariable: 'password',
                                      usernameVariable: 'username')]) {
      
    
                            def resp = sh(script: 'curl -v -X POST "http://localhost:8081/service/rest/v1/components?repository=Nexus_PROD" -H "accept: application/json" -H "Content-Type: multipart/form-data" -F "maven2.groupId=Nexus_PROD" -F "maven2.artifactId='+{ID_system}+'" -F "maven2.version='+{version}+'" -F "maven2.generate-pom=true" -F "maven2.packaging=zip" -F "maven2.asset1=@file.zip;type=application/zip" -F "maven2.asset1.classifier=distrib" -F "maven2.asset1.extension=zip" -u ${username}:${password}', returnStdout: true)        
                                echo "Response: "+resp.toString()
                            if(!(resp =~ '204' || resp =~ '200')){


                                currentBuild.result='ABORTED'
                                return
                            }    
                    }
                    }catch(Exception ee){
                            echo 'EXCEPTION: ' + ee.getMessage()
                            currentBuild.result='ABORTED'
                            return
                    }  

                }
            }
        }
            
    }
    post{
        success{

     
                sh(script:"ls -l")
                echo 'Clean WorkFlow'
                cleanWs()

        }
    }

}