"""Views file."""

# -*- coding: utf-8 -*-
from __future__ import unicode_literals

import re
import json  
from django.http import JsonResponse
from django.http import HttpResponse, HttpResponseRedirect
from django.core.urlresolvers import reverse_lazy
from django.shortcuts import render
from django.shortcuts import get_object_or_404
from django.forms import formset_factory
from django.conf import settings
from .models import Project, Reports, UseCase, Action, Jobs, JobUseCases
from .forms import ActionsFormset, ProjectForm, UsecaseForm, JobsForm
from functools import partial, wraps
from django.http import JsonResponse
from django.http import FileResponse


def project_view(request):
    """Project screen views."""
    if request.method == 'GET':
        projects = Project.objects.all()
        form = ProjectForm()
        project_forms = zip(projects, [ProjectForm(instance=project_obj) for project_obj in projects])
        context = {
            'project_forms': project_forms,
            'form': form,
        }
        return render(request, "index.html", context)
    elif request.method == 'POST':
        project_id = int(request.POST.get('project_id', None))
        project_form = ProjectForm(data=request.POST)
        if project_form.is_valid():
            Project.objects.filter(id=project_id).update(name=project_form.cleaned_data['name'])
        return HttpResponseRedirect(reverse_lazy('hakuna_matata:projects'))


def reports_view(request, project_id):
    """Report screen views."""
    usecases = UseCase.objects.filter(project__id=project_id)
    context = {
        "usecases": usecases,
    }
    return render(request, "reports_list.html", context)


def show_all_reports(request, usecase_id):
    """Show all reports for a corresponding usecase."""
    all_reports = Reports.objects.filter(use_case_id=usecase_id)
    context = {
        "all_reports": all_reports,
    }
    return render(request, "all_reports.html", context)


def render_report(request, report_id):
    """Show reports views."""
    report_obj = get_object_or_404(Reports, pk=report_id)
    return render(request, report_obj.report)


def add_project(request):
    """Add new project."""
    if request.POST:
        new_project = ProjectForm(request.POST)
        if new_project.is_valid():
            new_project.save()
    return HttpResponseRedirect(reverse_lazy('hakuna_matata:projects'))


def remove_project(request, project_id):
    """Remove project."""
    remove_project = get_object_or_404(Project, pk=project_id)
    remove_project.delete()
    return HttpResponseRedirect(reverse_lazy('hakuna_matata:projects'))


def usecases_view(request, project_id):
    """Usecase screen views."""
    try:
        project = Project.objects.get(id=project_id).name
        usecases = UseCase.objects.filter(project__id=project_id).order_by('id')
        jobs = Jobs.objects.filter(project__id=project_id).order_by('id')
    except (UseCase.DoesNotExist, Project.DoesNotExist):
        return HttpResponse(500)
    if request.method == 'GET':
        usecases_form = zip(usecases, [UsecaseForm(instance=usecase) for usecase in usecases])
        add_usecase_form = UsecaseForm()
        jobs_form = zip(jobs, [JobsForm(instance=job) for job in jobs])
        add_jobs_form = JobsForm()
        context = {
            'project_id': project_id,
            'project': project,
            'usecases': usecases,
            'usecases_form': usecases_form,
            'has_usecases': bool(usecases),
            'url': settings.JAVA_API_URL,
            'add_usecase_form': add_usecase_form,
            'add_jobs_form': add_jobs_form,
            'jobs': jobs,
            'jobs_form': jobs_form,
            'has_jobs': bool(jobs),
        }
        return render(request, 'project_usecases.html', context)
    elif request.method == 'POST':
        usecase_id = request.POST.get('usecase_id', None)
        usecase_form = UsecaseForm(data=request.POST)
        if usecase_form.is_valid():
            UseCase.objects.filter(id=usecase_id).update(
                use_case_name=usecase_form.cleaned_data['use_case_name'],
                use_case_description=usecase_form.cleaned_data['use_case_description'],
            )

        job_id = request.POST.get('job_id', None)
        job_form = JobsForm(data=request.POST)
        if job_form.is_valid():
            Jobs.objects.filter(id=job_id).update(
                name=job_form.cleaned_data['name'],
            )
        return HttpResponseRedirect(reverse_lazy('hakuna_matata:usecases', kwargs={'project_id': project_id}))


def add_usecases_view(request, project_id):
    """View to handle newly added usecases."""
    usecase_form = UsecaseForm(data=request.POST)
    if usecase_form.is_valid():
        clean_data = usecase_form.cleaned_data
        if any(clean_data.values()):
            project = Project.objects.get(id=project_id)
            UseCase.objects.create(project=project, use_case_name=clean_data['use_case_name'], use_case_description=clean_data['use_case_description'])
    return HttpResponseRedirect(reverse_lazy('hakuna_matata:usecases', kwargs={'project_id': project_id}))


def clone_usecase_view(request, project_id, use_id):
    """View to clone the usecases."""

    usecase_form = UsecaseForm(data=request.POST)
    if usecase_form.is_valid():
        clean_data = usecase_form.cleaned_data
        if any(clean_data.values()):
            project = Project.objects.get(id=project_id)
            usecase_object = UseCase.objects.create(project=project, use_case_name=clean_data['use_case_name'], use_case_description=clean_data['use_case_description'])
            usecase_object.save()
            id = usecase_object.id
            actions_obj = Action.objects.filter(use_case__id=use_id).order_by('seq')
            for act in actions_obj.all():
                action_obj = Action()
                action_obj.seq = int(act.seq)
                action_obj.use_case = usecase_object
                action_obj.description = act.description
                action_obj.action = act.action
                action_obj.locators = act.locators
                action_obj.element_identifier = act.element_identifier
                action_obj.element_value = act.element_value
                action_obj.save()

    return HttpResponseRedirect(reverse_lazy('hakuna_matata:usecases', kwargs={'project_id': project_id}))


def delete_usecases_view(request, project_id):
    """View to handle deleted usecases."""
    for usecase_id in request.POST.get('deleted_usecases', '').strip(';').split(';'):
        if usecase_id:
            try:
                UseCase.objects.get(id=int(usecase_id)).delete()
            except UseCase.DoesNotExist:
                pass  # Object must have already been deleted.
    return HttpResponseRedirect(reverse_lazy('hakuna_matata:usecases', kwargs={'project_id': project_id}))


def add_jobs_view(request, project_id):
    """View to handle newly added jobs."""
    jobs_form = JobsForm(data=request.POST)
    if jobs_form.is_valid():
        clean_data = jobs_form.cleaned_data
        if any(clean_data.values()):
            project = Project.objects.get(id=project_id)
            Jobs.objects.create(project=project, name=clean_data['name'])
    return HttpResponseRedirect(reverse_lazy('hakuna_matata:usecases', kwargs={'project_id': project_id}))


def job_view(request, project_id, job_id):
    """Job screen View."""
    job_obj = get_object_or_404(Jobs, pk=job_id)
    if request.method == 'GET':
        usecases_in_job = JobUseCases.objects.filter(job=job_obj).order_by('seq')
        existing_usecases = []
        if usecases_in_job:
            existing_usecases = usecases_in_job.values('usecase_id')
        usecases_not_in_job = UseCase.objects.filter(project__id=project_id).exclude(id__in=existing_usecases).order_by('id')
        context = {
            'project_id': project_id,
            'job_id': job_id,
            'job_name': job_obj.name,
            'usecases_in_job': usecases_in_job,
            'job_has_usecases': bool(usecases_in_job),
            'project_has_usecases': bool(usecases_not_in_job),
            'usecases_not_in_job': usecases_not_in_job
        }
        return render(request, 'job.html', context)
    elif request.method == 'POST':
        usecases_to_add = [int(request.POST[key]) for key in request.POST if key.startswith('usecase_')]
        number_of_usecas_in_job = JobUseCases.objects.filter(job=job_obj).count()
        for seq, usecase_id in enumerate(usecases_to_add):
            try:
                usecase_obj = UseCase.objects.get(id=usecase_id)
            except UseCase.DoesNotExist:
                pass  # Add rest of the usecases to the job even if some of them fail.
            else:
                JobUseCases.objects.create(usecase=usecase_obj, job=job_obj, seq=seq + number_of_usecas_in_job + 1)
        return HttpResponseRedirect(reverse_lazy('hakuna_matata:job', kwargs={'project_id': project_id, 'job_id': job_id}))


def delete_job_usecases_view(request, project_id, job_id):
    """View to delete the usecases in a job."""
    job_obj = get_object_or_404(Jobs, pk=job_id)
    usecase_sequences = list(set([re.search("(job_usecase-\d+-seq)", x).group(0) for x in request.POST.keys() if re.search("job_usecase-\d+-seq", x)]))
    usecase_sequences.sort(key=lambda name: [int(number) for number in re.findall('\d+', name)])
    usecases_to_delete = [int(request.POST[key]) for key in request.POST if key.startswith('job_usecase_')]
    job_usecacses = JobUseCases.objects.filter(job=job_obj).order_by('usecase_id')
    for job_usecase_obj, usecase_sequence in zip(job_usecacses, usecase_sequences):
        try:
            usecase_obj = UseCase.objects.get(id=job_usecase_obj.usecase_id)
        except UseCase.DoesNotExist:
            pass  # Usecase itself must have been deleted.
        else:
            if job_usecase_obj.usecase_id in usecases_to_delete:
                JobUseCases.objects.get(usecase=usecase_obj, job=job_obj).delete()
            else:
                JobUseCases.objects.filter(usecase=usecase_obj, job=job_obj).update(seq=request.POST[usecase_sequence])
    return HttpResponseRedirect(reverse_lazy('hakuna_matata:job', kwargs={'project_id': project_id, 'job_id': job_id}))


def delete_job_view(request, project_id, job_id):
    """Remove project."""
    delete_job = get_object_or_404(Jobs, pk=job_id)
    delete_job.delete()
    return HttpResponseRedirect(reverse_lazy('hakuna_matata:usecases', kwargs={'project_id': project_id}))


def actions_view(request, project_id, usecase_id):
    """Action screen views."""
    actions_prefix = 'actions-prefix'
    actions_formset = formset_factory(wraps(ActionsFormset)(partial(
        ActionsFormset)),
        max_num=1000,
        extra=0,
        min_num=1
    )

    if request.method == 'GET':
        actions_obj = Action.objects.filter(use_case__id=usecase_id).order_by('seq')
        initial_data = []
        for obj in actions_obj:
            initial_data.append({
                'hidden_id': obj.id,
                'seq': obj.seq,
                'description': obj.description,
                'action': obj.action,
                'locators': obj.locators,
                'element_identifier': obj.element_identifier,
                'element_value': obj.element_value,
            })
        if not initial_data:
            initial_data = [{'seq': 1}]
        actions_form = actions_formset(initial=initial_data, prefix=actions_prefix)
        context = {
            'project_id': project_id,
            'usecase_id': usecase_id,
            'usecase_name': UseCase.objects.get(id=usecase_id).use_case_name,
            'actions_form': actions_form,
        }
        return render(request, 'usecase_actions.html', context)
    elif request.method == 'POST':
        for action_id in request.POST.get('deleted_actions', '').strip(';').split(';'):
            if action_id:
                try:
                    Action.objects.get(id=int(action_id)).delete()
                except Action.DoesNotExist:
                    pass  # Object must have already been deleted.
        actions_form = actions_formset(data=request.POST, prefix=actions_prefix)

        usecase_obj = UseCase.objects.get(id=usecase_id)
        if actions_form.is_valid():
            for action in actions_form.cleaned_data:
                if any(action.values()):
                    if not action['hidden_id']:
                        action_obj = Action()
                        action_obj.seq = int(action['seq'])
                        action_obj.use_case = usecase_obj
                        action_obj.description = action['description']
                        action_obj.action = action['action']
                        action_obj.locators = action['locators']
                        action_obj.element_identifier = action['element_identifier']
                        action_obj.element_value = action['element_value']
                        action_obj.save()
                    else:
                        action_obj = Action.objects.filter(id=int(action['hidden_id']))
                        action_obj.update(
                            seq=int(action['seq']),
                            description=action['description'],
                            action=action['action'],
                            locators=action['locators'],
                            element_identifier=action['element_identifier'],
                            element_value=action['element_value'],
                        )
        return HttpResponseRedirect(reverse_lazy('hakuna_matata:actions', kwargs={'project_id': project_id, 'usecase_id': usecase_id}))


def export_usecase(request, usecase_id):
    """Export db data to a json file"""
    filtered_entries = Action.objects.filter(use_case=usecase_id)
    usecase_name=UseCase.objects.get(id=usecase_id)
  
    data = {}
    for entry in filtered_entries:
        children = [] 
        children.append({"description":entry.description, "action":entry.action, "locators":entry.locators, "element_identifier":entry.element_identifier, "element_value":entry.element_value, "use_case_id":entry.use_case_id })   
        data[entry.seq] = children
    filePathNameWExt = './JsonFiles/export_data.json'

    with open(filePathNameWExt, 'w') as fp:
        json.dump(data, fp)

    response = HttpResponse(open(filePathNameWExt,"rb"), content_type='application/json')
    response['Content-Disposition'] = 'attachment; filename='+usecase_name.use_case_name+'.json'
    return response

