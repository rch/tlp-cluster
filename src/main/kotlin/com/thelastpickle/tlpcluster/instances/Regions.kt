package com.thelastpickle.tlpcluster.instances

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.thelastpickle.tlpcluster.YamlDelegate
import com.thelastpickle.tlpcluster.instances.importers.InstancesImporter
import com.thelastpickle.tlpcluster.instances.importers.RegionImporter
import com.thelastpickle.tlpcluster.instances.importers.UbuntuImporter
import org.apache.logging.log4j.kotlin.logger


/**
 * Container and loader for region specific information
 * Here we can grab all the regions and their cooresponding amis and azs
 *
 * This file is generated from other locations
 */
data class Regions(val regions: Map<String, Region>) {

    // maps instance type to ami
    data class Region(val azs: List<String>, val amis: Map<String, String>)

    companion object {
        val log = logger()
        val yaml : ObjectMapper by YamlDelegate()
        val url = this::class.java.getResource("regions.yaml")!!

        fun load() : Regions {
            val regions = yaml.readValue<Regions>(url)
            return regions
        }

        /**
         * Regenerates the local file
         */
        fun regenerate() {
            val regionImporter = RegionImporter.loadFromJson()
            val ubuntu = UbuntuImporter.loadFromResource()
            val instances = InstancesImporter.loadFromCSV()

            for(region in regionImporter.regions) {
                // get all the AZs
                // for each instance type get the right ami
//                val azs = regionImporter.regions.
            }

        }


    }

    operator fun get(region: String) : Region? {
        return regions[region]
    }

}

