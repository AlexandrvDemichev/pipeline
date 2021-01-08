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
                    try{
                        def resp = sh(script: 'PGPASSWORD=postgres psql -h 127.0.0.1 -p 5432 -U postgres -d nexus_distrib_monitoring -c "select * from distrib_list where id_system = \''+id_system+'\'"', returnStdout: true)
                        
                        echo resp
                        if(resp =~ '0 rows'){
                            currentBuild.result='ABORTED'
                            return
                        }
                    }catch(Exception ee){
                            echo 'EXCEPTION: ' + ee.getMessage()
                            currentBuild.result='ABORTED'
                            return
                    }
                        
                }
            }
        }
        
        stage('API distrib monitoring') {
            steps{
                script{
                    if(currentBuild.result!='ABORTED'){
                        echo 'API distrib monitoring'
                        try{
                         def resp = sh(script: 'curl -v -X POST "127.0.0.1:5000/api/deploy_psi?id=${id_system}&version=${version}&path=Nexus_PROD/${id_system}/${version}/${id_system}-${version}-distrib.zip" ', returnStdout: true)        
                                echo "Response: "+resp.toString()
                            if(!(resp =~ '"code":0')){


                                currentBuild.result='ABORTED'
                                return
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
        
            
    }
    post{
        success{

     
                sh(script:"ls -l")
                echo 'Clean WorkFlow'
                cleanWs()

        }
    }

}