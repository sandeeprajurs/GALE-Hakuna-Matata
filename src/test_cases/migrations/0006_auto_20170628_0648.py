# -*- coding: utf-8 -*-
# Generated by Django 1.11.2 on 2017-06-28 06:48
from __future__ import unicode_literals

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('test_cases', '0005_action_seq'),
    ]

    operations = [
        migrations.AlterModelOptions(
            name='action',
            options={'ordering': ['use_case__project__name', 'use_case__use_case_name', 'seq']},
        ),
        migrations.AlterModelOptions(
            name='project',
            options={'ordering': ['name']},
        ),
        migrations.AlterModelOptions(
            name='usecase',
            options={'ordering': ['project__name', 'use_case_name']},
        ),
        migrations.AlterField(
            model_name='action',
            name='action',
            field=models.CharField(max_length=255),
        ),
        migrations.AlterField(
            model_name='action',
            name='locators',
            field=models.CharField(blank=True, max_length=255),
        ),
    ]
