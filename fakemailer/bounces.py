#!/usr/bin/env python

import os
import json

bounceFolder = "./bounces/"
resultFile = "bounces.json"

class BounceJsonCreator:
    def create(self):
        bounces = self.read_bounces_data()
        json_data = { "bounced_emails" : self.convert(bounces) }
        data = json.dumps(json_data, sort_keys=False, indent=2)
        self.write_result(data)

    def read_bounces_data(self):
        bounces = []
        for root, dirs, filenames in os.walk(bounceFolder):
            for f in filenames:
                if f.endswith(".csv"):
                    bounceCsv = open(os.path.join(root, f),'r')
                    bounceData = bounceCsv.read().split(";")
                    bounces.append(bounceData)
        return bounces

    def convert(self, bounces):
        return map(lambda x: { "email" : x[0], "bounce_cnt" : 1, "last_bounce" : x[1], "X-Batch-ID": "", "X-Message-ID": x[2]}, bounces)

    def write_result(self, json):
         file = open(bounceFolder + resultFile, "w")
         file.write(json)
         file.close

if __name__ == "__main__":
    creator = BounceJsonCreator()
    creator.create()