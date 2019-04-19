package com.thelastpickle.tlpcluster.commands

import com.beust.jcommander.Parameter
import com.thelastpickle.tlpcluster.Context
import com.thelastpickle.tlpcluster.configuration.HostList
import com.thelastpickle.tlpcluster.configuration.ServerType
import java.io.FileNotFoundException

class Hosts(val context: Context) : ICommand {


    @Parameter(names = ["-c"], description = "Show Cassandra as a comma delimited list")
    var cassandra : Boolean = false

    data class HostOutput(val cassandra: HostList, val stress: HostList, val monitoring: HostList)

    override fun execute() {
        try {
            val output = with(context.tfstate) {
                    HostOutput(getHosts(ServerType.Cassandra),
                            getHosts(ServerType.Stress),
                            getHosts(ServerType.Monitoring))
            }

            if(cassandra) {
                val hosts = context.tfstate.getHosts(ServerType.Cassandra)
                val csv = hosts.map { it.public }.joinToString(",")
                println(csv)
            } else {
                context.yaml.writeValue(System.out, output)
            }
        } catch (e: FileNotFoundException) {
            println("terraform.tfstate does not exist yet, most likely tlp-cluster up has not been run.")
        }
    }
}