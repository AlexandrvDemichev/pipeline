//package main
def tmp_group_id = 'Nexus_PROD'
pipeline {
    agent any
    parameters {
        string(defaultValue: '', description: 'Версия дистрибутива', name: 'version')
        string(defaultValue: 'ID000000', description: 'ID системы', name: 'ID_system')
        file(name:'upload_file.txt', description:'Загрузить файл')
    }
    options{

        timestamps()
    }
    stages {
        stage('Get upload file') {
            steps{
                script{
                    echo 'Get upload file'
                    sh 'ls -l'
                    sh 'cat upload_file.txt'
                        
                }
            }
        }
        
        stage('Build archive') {
            steps{
                script{
                    echo 'Build archive'
                    
                }
            }
        }
        stage('Upload file to nexus') {
            steps{
                script{
                    echo 'Upload file to nexus'
                    
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