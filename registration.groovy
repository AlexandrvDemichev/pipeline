def tmp_group_id = 'Nexus_PROD'
pipeline {
    agent any
    parameters {
        string(defaultValue: '', description: 'Версия дистрибутива', name: 'version')
        string(defaultValue: 'ID000000', description: 'ID системы', name: 'id_system')
        
    }
    options{

        timestamps()
    }
    stages {
        stage('Check distrib') {
            steps{
                script{
                    echo 'Search distrib'
                    def resp = sh(script: 'psql -version', returnStdout: true)
                    sleep(3)
                    echo resp
                        
                }
            }
        }
        
        stage('API distrib monitoring') {
            steps{
                script{
                    echo 'API distrib monitoring'
                    try{
                     
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