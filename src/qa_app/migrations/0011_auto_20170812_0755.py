# -*- coding: utf-8 -*-
# Generated by Django 1.11.2 on 2017-08-12 07:55
from __future__ import unicode_literals

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('qa_app', '0010_auto_20170812_0750'),
    ]

    operations = [
        migrations.AlterField(
            model_name='action',
            name='element_value',
            field=models.CharField(max_length=1024),
        ),
    ]
